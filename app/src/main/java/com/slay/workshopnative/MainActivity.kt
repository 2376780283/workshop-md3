package com.slay.workshopnative

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slay.workshopnative.core.logging.MainActivityRuntimeTracker
import com.slay.workshopnative.ui.MainViewModel
import com.slay.workshopnative.ui.WorkshopNativeRoot
import com.slay.workshopnative.ui.theme.WorkshopNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val instanceId: String = Integer.toHexString(System.identityHashCode(this))
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityRuntimeTracker.onCreated(
            instanceId = instanceId,
            taskId = taskId,
            intentFlags = intent?.flags,
            hadSavedState = savedInstanceState != null,
        )
        enableEdgeToEdge()
        setContent {
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            
            val themeMode = viewModel.themeMode.collectAsStateWithLifecycle().value
            WorkshopNativeTheme(themeMode = themeMode) {
                AnimatedVisibility(visible = visible, enter = fadeIn(tween(1000))) {
                    WorkshopNativeRoot(viewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        MainActivityRuntimeTracker.onNewIntent(
            instanceId = instanceId,
            taskId = taskId,
            intentFlags = intent.flags,
        )
    }

    override fun onResume() {
        super.onResume()
        MainActivityRuntimeTracker.onResumed(instanceId = instanceId, taskId = taskId)
    }

    override fun onStop() {
        MainActivityRuntimeTracker.onStopped(instanceId = instanceId, taskId = taskId)
        super.onStop()
    }

    override fun onDestroy() {
        MainActivityRuntimeTracker.onDestroyed(
            instanceId = instanceId,
            taskId = taskId,
            changingConfigurations = isChangingConfigurations,
        )
        super.onDestroy()
    }
}
