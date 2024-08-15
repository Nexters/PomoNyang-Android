package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
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

        composable<PomodoroTimer> { backStackEntry ->
            PomodoroTimerRoute()
        }
    }
}
