package com.pomonyang.mohanyang

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.startup.AppInitializer
import app.rive.runtime.kotlin.RiveInitializer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
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

    @Inject
    lateinit var pomodoroTimerRepository: PomodoroTimerRepository

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppInitializer.getInstance(applicationContext)
            .initializeComponent(RiveInitializer::class.java)
        createNotificationChannel()
        registerNotificationService()
        GlobalScope.launch { pomodoroTimerRepository.savePomodoroCacheData() }
        handleSplashScreen()
        initializeFCM()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val activity = (LocalContext.current as? Activity)

            val isNewUser = userRepository.isNewUser()
            var showDialog by remember { mutableStateOf(isNewUser && !networkMonitor.isConnected) }
            suspend fun initializeInfo() {
                getTokenByDeviceIdUseCase()
                    .onSuccess {
                        userRepository.fetchMyInfo()
                        pomodoroSettingRepository.fetchPomodoroSettingList()
                        showDialog = false
                    }.onFailure {
                        showDialog = isNewUser
                    }
            }

            runBlocking { initializeInfo() }

            MnTheme {
                val mohaNyangAppState = rememberMohaNyangAppState(
                    isNewUser = isNewUser,
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
                                onClick = {
                                    coroutineScope.launch {
                                        initializeInfo()
                                    }
                                },
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

    override fun onResume() {
        super.onResume()
        MnNotificationManager.stopInterrupt(this)
    }

    override fun onDestroy() {
        GlobalScope.launch { pomodoroTimerRepository.updatePomodoroDone() }
        super.onDestroy()
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

    private fun handleSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        setSplashAnimation()
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, Companion.SPLASH_DELAY)
    }

    private fun setSplashAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->

                val startColor = ContextCompat.getColor(splashScreenView.context, R.color.splash_background)
                val endColor = ContextCompat.getColor(splashScreenView.context, R.color.splash_delay_background)

                val colorAnim = ValueAnimator.ofArgb(startColor, Color.WHITE, endColor).apply {
                    duration = 300L
                    addUpdateListener { animator ->
                        val animatedColor = animator.animatedValue as Int
                        splashScreenView.setBackgroundColor(animatedColor)
                    }
                }

                val iconFadeOut = ObjectAnimator.ofFloat(splashScreenView.iconView, View.ALPHA, 1f, 0f).apply {
                    duration = 200L
                }

                AnimatorSet().apply {
                    playTogether(iconFadeOut, colorAnim)
                    interpolator = AnticipateInterpolator()
                    doOnEnd {
                        splashScreenView.remove()
                    }
                    start()
                }
            }
        }
    }

    companion object {
        private const val SPLASH_DELAY = 1500L
    }
}
