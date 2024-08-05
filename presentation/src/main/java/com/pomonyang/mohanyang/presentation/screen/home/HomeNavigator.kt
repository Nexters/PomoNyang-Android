package com.pomonyang.mohanyang.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroSetting
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.homeScreen(
    navigateUp: () -> Unit,
    navHostController: NavHostController
) {
    composable<Home> {
        HomeRoute(
            onNavigationClick = navigateUp,
            onPomodoroSettingClick = { navHostController.navigate(PomodoroSetting) }
        )
    }
}
