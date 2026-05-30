package com.slay.workshopnative.ui.feature.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slay.workshopnative.data.model.AuthChallengeType
import com.slay.workshopnative.data.model.SessionStatus
import com.slay.workshopnative.data.model.SteamSessionState
import com.slay.workshopnative.data.preferences.SavedSteamAccount

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

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier.fillMaxSize().imePadding().padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState =
                    when {
                        isBusy -> "busy"
                        awaitingCode -> "auth"
                        else -> "login"
                    },
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "LoginStateTransition",
            ) { targetState ->
                Card(
                    modifier = Modifier.widthIn(max = 430.dp).animateContentSize(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    when (targetState) {
                        "busy" -> {
                            Column(
                                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = busyLabel,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        "auth" -> {
                            AuthForm(
                                sessionState = sessionState,
                                authCode = authCode,
                                onAuthCodeChange = { authCode = it },
                                onSubmitAuthCode = onSubmitAuthCode,
                            )
                        }
                        "login" -> {
                            LoginForm(
                                username = username,
                                onUsernameChange = { username = it },
                                password = password,
                                onPasswordChange = { password = it },
                                rememberSession = rememberSession,
                                onRememberSessionChange = { rememberSession = it },
                                inputsEnabled = inputsEnabled,
                                onLogin = onLogin,
                                onContinueAsGuest = onContinueAsGuest,
                                passwordVisible = passwordVisible,
                                onPasswordVisibleChange = { passwordVisible = it },
                                savedAccounts = savedAccounts,
                                onSwitchSavedAccount = onSwitchSavedAccount,
                                sessionState = sessionState,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthForm(
    sessionState: SteamSessionState,
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    onSubmitAuthCode: (String) -> Unit,
) {
    val challenge = sessionState.challenge
    if (challenge == null) {
        // Handle unexpected null challenge gracefully
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    
    val title =
        when (challenge.type) {
            AuthChallengeType.SteamGuard -> "Steam Guard"
            AuthChallengeType.Email -> challenge.emailHint?.let { "邮箱验证码 · $it" } ?: "邮箱验证码"
        }

    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        if (challenge.previousCodeIncorrect) {
            Text(
                text = "验证码不正确，请重新输入。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        OutlinedTextField(
            value = authCode,
            onValueChange = onAuthCodeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("验证码") },
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
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = authCode.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("验证并登录", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    rememberSession: Boolean,
    onRememberSessionChange: (Boolean) -> Unit,
    inputsEnabled: Boolean,
    onLogin: (String, String, Boolean) -> Unit,
    onContinueAsGuest: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    savedAccounts: List<SavedSteamAccount>,
    onSwitchSavedAccount: (String) -> Unit,
    sessionState: SteamSessionState,
) {
    Column(
        modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "登录 Workshop Native",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        sessionState.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Steam 用户名") },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Person, contentDescription = null) },
            enabled = inputsEnabled,
            singleLine = true,
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("密码") },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(
                        imageVector =
                            if (passwordVisible) Icons.Rounded.VisibilityOff
                            else Icons.Rounded.Visibility,
                        contentDescription = null,
                    )
                }
            },
            enabled = inputsEnabled,
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "记住登录状态", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = rememberSession, onCheckedChange = onRememberSessionChange)
        }

        Button(
            onClick = { onLogin(username, password, rememberSession) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = username.isNotBlank() && password.isNotBlank() && inputsEnabled,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("登录 Steam", fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = inputsEnabled,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("匿名访问", fontWeight = FontWeight.SemiBold)
        }

        if (savedAccounts.isNotEmpty()) {
            Text(
                text = "已保存账号",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
            savedAccounts.forEach { account ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = account.accountName,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text =
                                    if (account.steamId64 > 0L) "SteamID ${account.steamId64}"
                                    else "已保存登录态",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        FilledTonalButton(
                            onClick = { onSwitchSavedAccount(account.stableKey()) },
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
