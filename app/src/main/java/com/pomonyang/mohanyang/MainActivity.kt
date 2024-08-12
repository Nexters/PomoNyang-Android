package com.pomonyang.mohanyang

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetTokenByDeviceIdUseCase
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.ui.MohaNyangApp
import com.pomonyang.mohanyang.ui.rememberMohaNyangAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var pomodoroSettingRepository: PomodoroSettingRepository

    @Inject
    lateinit var getTokenByDeviceIdUseCase: GetTokenByDeviceIdUseCase

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleSplashScreen()
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                getTokenByDeviceIdUseCase()
                pomodoroSettingRepository.fetchPomodoroSettingList()
            }

            MnTheme {
                val coroutineScope = rememberCoroutineScope()
                val mohaNyangAppState = rememberMohaNyangAppState(
                    isNewUser = userRepository.isNewUser(),
                    networkMonitor = networkMonitor,
                    coroutineScope = coroutineScope
                )

                MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
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
}
