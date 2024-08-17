package com.pomonyang.mohanyang

import android.app.Activity
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetTokenByDeviceIdUseCase
import com.pomonyang.mohanyang.notification.LocalNotificationReceiver
import com.pomonyang.mohanyang.notification.util.createNotificationChannel
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import com.pomonyang.mohanyang.ui.MohaNyangApp
import com.pomonyang.mohanyang.ui.rememberMohaNyangAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var pomodoroSettingRepository: PomodoroSettingRepository

    @Inject
    lateinit var pushAlarmRepository: PushAlarmRepository

    @Inject
    lateinit var getTokenByDeviceIdUseCase: GetTokenByDeviceIdUseCase

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        registerNotificationService()

        handleSplashScreen()
        initializeFCM()
        enableEdgeToEdge()

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val activity = (LocalContext.current as? Activity)

            val isNewUser = userRepository.isNewUser()
            var showDialog by remember { mutableStateOf(isNewUser && !networkMonitor.isConnected) }

            fun initializeInfo() {
                coroutineScope.launch {
                    getTokenByDeviceIdUseCase().onSuccess {
                        userRepository.fetchMyInfo()
                        pomodoroSettingRepository.fetchPomodoroSettingList()
                    }.onFailure {
                        showDialog = isNewUser
                    }
                }
            }

            LaunchedEffect(Unit) {
                initializeInfo()
            }

            MnTheme {
                val mohaNyangAppState = rememberMohaNyangAppState(
                    isNewUser = userRepository.isNewUser(),
                    networkMonitor = networkMonitor,
                    coroutineScope = coroutineScope
                )
                if (showDialog) {
                    MnDialog(
                        properties = DialogProperties(
                            dismissOnClickOutside = false,
                            dismissOnBackPress = true
                        ),
                        title = "네트워크 연결 실패",
                        subTitle = "네트워크 연결에 실패하였습니다.\n확인 후 다시 시도해 주세요",
                        positiveButton = {
                            MnBoxButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = "새로고침",
                                onClick = { initializeInfo() },
                                colors = MnBoxButtonColorType.primary,
                                styles = MnBoxButtonStyles.medium
                            )
                        },
                        onDismissRequest = {
                            activity?.finish()
                        }
                    )
                } else {
                    MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
                }
            }
        }
    }

    private fun handleSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, Companion.SPLASH_DELAY)
    }

    companion object {
        private const val SPLASH_DELAY = 2000L
    }

    override fun onResume() {
        super.onResume()
        MnNotificationManager.stopInterrupt(this)
    }

    override fun onPause() {
        super.onPause()
        MnNotificationManager.startInterrupt(this)
    }

    private fun initializeFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Timber.d("FCM : $token")
            }
        )
    }

    private fun registerNotificationService() {
        val intentFilters = IntentFilter().apply {
            MnNotificationManager.intents.map { addAction(it) }
        }
        val receiver = LocalNotificationReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilters)
    }
}
