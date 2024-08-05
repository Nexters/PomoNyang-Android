package com.pomonyang.mohanyang.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroSetting
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.pomodoroSettingScreen
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.homeScreen(
    navigateUp: () -> Unit,
    isNewUser: Boolean,
    navHostController: NavHostController
) {
    composable<Home> {
        HomeRoute(
            onNavigationClick = navigateUp,
            onPomodoroSettingClick = { navHostController.navigate(PomodoroSetting) }
        )
    }

    pomodoroSettingScreen(isNewUser)
}
