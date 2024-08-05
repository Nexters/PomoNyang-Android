package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pomonyang.mohanyang.presentation.screen.home.Home
import kotlinx.serialization.Serializable

@Serializable
data object PomodoroSetting

fun NavGraphBuilder.pomodoroSettingScreen(isNewUser: Boolean) {
    composable<Home> {
        PomodoroSettingRoute(isNewUser)
    }
}
