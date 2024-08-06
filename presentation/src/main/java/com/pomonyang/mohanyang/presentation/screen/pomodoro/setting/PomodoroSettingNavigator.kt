package com.pomonyang.mohanyang.presentation.screen.pomodoro.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object PomodoroSetting

fun NavGraphBuilder.pomodoroSettingScreen(isNewUser: Boolean) {
    composable<PomodoroSetting> {
        PomodoroSettingRoute(isNewUser)
    }
}