package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestWaitingRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroSettingRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.time.PomodoroTimeSettingRoute
import kotlinx.serialization.Serializable

@Serializable
data object Pomodoro

@Serializable
internal data object PomodoroSetting

@Serializable
internal data class TimeSetting(val isFocusTime: Boolean, val initialTime: Int)

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
    onShowSnackbar: (String, String?) -> Unit,
    onBackPressed: () -> Unit,
    navHostController: NavHostController
) {
    navigation<Pomodoro>(
        startDestination = PomodoroSetting
    ) {
        composable<PomodoroSetting> {
            PomodoroSettingRoute(
                isNewUser = isNewUser,
                onShowSnackbar = onShowSnackbar,
                goTimeSetting = { isFocusTime, initialTime ->
                    navHostController.navigate(
                        TimeSetting(isFocusTime, initialTime)
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
                onShowSnackbar = onShowSnackbar
            ) {
                navHostController.popBackStack()
            }
        }

        composable<PomodoroTimer> {
            PomodoroTimerRoute(
                onBackPressed = onBackPressed,
                goToRest = { type, focusTime, exceededTime ->
                    navHostController.navigate(
                        route = PomodoroRestWaiting(
                            type = type,
                            focusTime = focusTime,
                            exceededTime = exceededTime
                        ),
                        navOptions = NavOptions.Builder().setPopUpTo<PomodoroTimer>(true).build()
                    )
                },
                goToHome = {
                    navHostController.navigate(PomodoroSetting) {
                        popUpTo(PomodoroSetting)
                    }
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
                    navHostController.navigate(
                        route = PomodoroSetting,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<PomodoroRestWaiting>(inclusive = true)
                            .setLaunchSingleTop(true)
                            .build()
                    )
                },
                goToPomodoroRest = { navHostController.navigate(PomodoroRest) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable<PomodoroRest> {
            PomodoroRestRoute(
                onBackPressed = onBackPressed,
                onShowSnackbar = onShowSnackbar,
                goToHome = {
                    navHostController.navigate(PomodoroSetting) {
                        popUpTo(PomodoroSetting)
                    }
                },
                goToFocus = {
                    navHostController.navigate(
                        route = PomodoroTimer,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<PomodoroRest>(inclusive = true)
                            .setLaunchSingleTop(true)
                            .build()
                    )
                }
            )
        }
    }
}
