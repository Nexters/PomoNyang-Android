package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestWaitingRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroSettingRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.time.PomodoroTimeSettingRoute
import kotlinx.serialization.Serializable

@Serializable
data class Pomodoro(
    val isNewUser: Boolean
)

@Serializable
internal data object PomodoroSetting

@Serializable
internal data class TimeSetting(val isFocusTime: Boolean, val initialTime: Int, val categoryName: String)

@Serializable
internal data object PomodoroTimer

@Serializable
internal data class PomodoroRestWaiting(
    val type: String,
    val focusTime: Int,
    val exceededTime: Int
)

@Serializable
internal data object PomodoroRest

fun NavGraphBuilder.pomodoroScreen(
    isNewUser: Boolean,
    onShowSnackbar: (String, Int?) -> Unit,
    navHostController: NavHostController
) {
    navigation<Pomodoro>(
        startDestination = PomodoroSetting
    ) {
        composable<PomodoroSetting> {
            PomodoroSettingRoute(
                isNewUser = isNewUser,
                onShowSnackbar = onShowSnackbar,
                goTimeSetting = { isFocusTime, initialTime, categoryName ->
                    navHostController.navigate(
                        TimeSetting(isFocusTime, initialTime, categoryName)
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
                initialSettingTime = routeData.initialTime,
                categoryName = routeData.categoryName,
                onShowSnackbar = onShowSnackbar
            ) {
                navHostController.popBackStack()
            }
        }

        composable<PomodoroTimer> {
            PomodoroTimerRoute(
                goToRest = { type, focusTime, exceededTime ->
                    navHostController.navigate(
                        PomodoroRestWaiting(
                            type = type,
                            focusTime = focusTime,
                            exceededTime = exceededTime
                        )
                    ) {
                        popUpTo(PomodoroTimer) { inclusive = true }
                    }
                },
                goToHome = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<PomodoroRestWaiting> { backStackEntry ->
            val routeData = backStackEntry.toRoute<PomodoroRestWaiting>()
            PomodoroRestWaitingRoute(
                type = routeData.type,
                focusTime = routeData.focusTime,
                exceedTime = routeData.exceededTime,
                goToHome = {
                    navHostController.popBackStack()
                },
                goToPomodoroRest = {
                    navHostController.navigate(PomodoroRest) {
                        popUpTo<PomodoroRestWaiting> { inclusive = true }
                    }
                },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable<PomodoroRest> {
            PomodoroRestRoute(
                onShowSnackbar = onShowSnackbar,
                goToHome = {
                    navHostController.popBackStack()
                },
                goToFocus = {
                    navHostController.navigate(PomodoroTimer) {
                        popUpTo(PomodoroRest) { inclusive = true }
                    }
                }
            )
        }
    }
}
