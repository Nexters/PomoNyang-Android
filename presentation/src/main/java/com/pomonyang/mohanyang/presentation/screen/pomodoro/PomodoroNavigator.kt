package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroSettingRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.time.PomodoroTimeSettingRoute
import kotlinx.serialization.Serializable

@Serializable
data object Pomodoro

@Serializable
internal data object PomodoroSetting

@Serializable
internal data class TimeSetting(val isFocusTime: Boolean)

@Serializable
internal data object PomodoroTimer

@Serializable
internal data object PomodoroRest

fun NavGraphBuilder.pomodoroScreen(
    isNewUser: Boolean,
    onShowSnackbar: (String, String?) -> Unit,
    navHostController: NavHostController
) {
    navigation<Pomodoro>(
        startDestination = PomodoroSetting
    ) {
        composable<PomodoroSetting> {
            PomodoroSettingRoute(
                isNewUser = isNewUser,
                onShowSnackbar = onShowSnackbar,
                goTimeSetting = { isFocusTime ->
                    navHostController.navigate(
                        TimeSetting(isFocusTime)
                    )
                },
                goToPomodoro = {
                    navHostController.navigate(PomodoroTimer)
                }
            )
        }

        composable<TimeSetting> { backStackEntry ->
            val routeData = backStackEntry.toRoute<TimeSetting>()
            PomodoroTimeSettingRoute(
                isFocusTime = routeData.isFocusTime,
                onShowSnackbar = onShowSnackbar
            ) {
                navHostController.popBackStack()
            }
        }

        composable<PomodoroTimer> {
            PomodoroTimerRoute(
                goToRest = {
                    navHostController.navigate(PomodoroRest) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        popUpTo(PomodoroTimer) {
                            inclusive = true
                        }
                        restoreState = true
                    }
                },
                goToPomodoroSetting = {
                    navHostController.navigate(PomodoroSetting) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<PomodoroRest> {
            PomodoroRestRoute()
        }
    }
}
