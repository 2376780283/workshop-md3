package com.slay.workshopnative.ui.feature.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slay.workshopnative.R
import com.slay.workshopnative.data.model.AuthChallengeType
import com.slay.workshopnative.data.model.SessionStatus
import com.slay.workshopnative.data.model.SteamSessionState
import com.slay.workshopnative.data.preferences.SavedSteamAccount
import com.slay.workshopnative.ui.components.WorkshopBackdrop
import com.slay.workshopnative.ui.theme.workshopAdaptiveBorderColor
import com.slay.workshopnative.ui.theme.workshopAdaptiveSurfaceColor

@Composable
fun LoginScreen(
    sessionState: SteamSessionState,
    savedAccounts: List<SavedSteamAccount>,
    onLogin: (String, String, Boolean) -> Unit,
    onSubmitAuthCode: (String) -> Unit,
    onSwitchSavedAccount: (String) -> Unit,
    onContinueAsGuest: () -> Unit,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var authCode by remember { mutableStateOf("") }
    var rememberSession by rememberSaveable { mutableStateOf(true) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(sessionState.account?.accountName) {
        if (username.isBlank()) {
            username = sessionState.account?.accountName.orEmpty()
        }
    }

    LaunchedEffect(
        sessionState.status,
        sessionState.challenge?.type,
        sessionState.challenge?.previousCodeIncorrect,
    ) {
        if (sessionState.status != SessionStatus.AwaitingCode) {
            authCode = ""
        }
    }

    val awaitingCode =
        sessionState.status == SessionStatus.AwaitingCode && sessionState.challenge != null
    val isBusy =
        sessionState.status == SessionStatus.Connecting ||
            sessionState.status == SessionStatus.Authenticating
    val inputsEnabled =
        !awaitingCode &&
            sessionState.status != SessionStatus.Connecting &&
            sessionState.status != SessionStatus.Authenticating
    val busyLabel =
        when (sessionState.status) {
            SessionStatus.Connecting -> "正在连接 Steam…"
            SessionStatus.Authenticating -> "正在验证登录信息…"
            else -> "正在处理…"
        }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier =
                Modifier.fillMaxSize().imePadding().padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().widthIn(max = 430.dp),
            ) {
                Column(
                    modifier =
                        Modifier.verticalScroll(rememberScrollState())
                            .padding(horizontal = 22.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "Workshop Native",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    
                    // ... (rest of the form content)


                        sessionState.errorMessage?.let { message ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = message,
                                    modifier =
                                        Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                )
                            }
                        }

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Steam 用户名") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                            },
                            enabled = inputsEnabled,
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next,
                                ),
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("密码") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector =
                                            if (passwordVisible) {
                                                Icons.Rounded.VisibilityOff
                                            } else {
                                                Icons.Rounded.Visibility
                                            },
                                        contentDescription = if (passwordVisible) "隐藏密码" else "显示密码",
                                    )
                                }
                            },
                            enabled = inputsEnabled,
                            singleLine = true,
                            visualTransformation =
                                if (passwordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        if (
                                            username.isNotBlank() &&
                                                password.isNotBlank() &&
                                                inputsEnabled
                                        ) {
                                            onLogin(username, password, rememberSession)
                                        }
                                    }
                                ),
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "记住登录状态",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Switch(
                                checked = rememberSession,
                                onCheckedChange = { rememberSession = it },
                            )
                        }

                        if (awaitingCode) {
                            val challenge = sessionState.challenge!!
                            val title =
                                when (challenge.type) {
                                    AuthChallengeType.SteamGuard -> "Steam Guard"
                                    AuthChallengeType.Email -> challenge.emailHint?.let { "邮箱验证码 · $it" } ?: "邮箱验证码"
                                }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                    )

                                    if (challenge.previousCodeIncorrect) {
                                        Text(
                                            text = "验证码不正确，请重新输入。",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    }

                                    OutlinedTextField(
                                        value = authCode,
                                        onValueChange = { authCode = it },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text("验证码") },
                                        leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
                                        singleLine = true,
                                        keyboardOptions =
                                            KeyboardOptions(
                                                keyboardType = KeyboardType.Ascii,
                                                capitalization = KeyboardCapitalization.Characters,
                                                autoCorrectEnabled = false,
                                                imeAction = ImeAction.Done,
                                            ),
                                        keyboardActions =
                                            KeyboardActions(onDone = { if (authCode.isNotBlank()) onSubmitAuthCode(authCode) }),
                                    )

                                    Button(
                                        onClick = { onSubmitAuthCode(authCode) },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = authCode.isNotBlank(),
                                    ) {
                                        Text("验证并登录", fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }

                        if (!awaitingCode) {
                            Button(
                                onClick = { onLogin(username, password, rememberSession) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(22.dp),
                                enabled =
                                    username.isNotBlank() && password.isNotBlank() && inputsEnabled,
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        disabledContainerColor =
                                            MaterialTheme.colorScheme.surfaceContainerHighest,
                                        disabledContentColor =
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                            ) {
                                Text(
                                    text = "登录 Steam",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }

                            OutlinedButton(
                                onClick = onContinueAsGuest,
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                enabled = inputsEnabled,
                                shape = RoundedCornerShape(20.dp),
                            ) {
                                Text(
                                    text = "匿名访问",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }

                            if (savedAccounts.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Text(
                                        text = "已保存账号",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    savedAccounts.forEach { account ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            Row(
                                                modifier =
                                                    Modifier.fillMaxWidth()
                                                        .padding(16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                                ) {
                                                    Text(
                                                        text = account.accountName,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        fontWeight = FontWeight.SemiBold,
                                                    )
                                                    Text(
                                                        text =
                                                            if (account.steamId64 > 0L) {
                                                                "SteamID ${account.steamId64}"
                                                            } else {
                                                                "已保存登录态"
                                                            },
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color =
                                                            MaterialTheme.colorScheme
                                                                .onSurfaceVariant,
                                                    )
                                                }
                                                FilledTonalButton(
                                                    onClick = {
                                                        onSwitchSavedAccount(account.stableKey())
                                                    },
                                                    enabled = inputsEnabled,
                                                ) {
                                                    Text("切换")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            if (isBusy) {
                Card(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = busyLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

// End of LoginScreen file

