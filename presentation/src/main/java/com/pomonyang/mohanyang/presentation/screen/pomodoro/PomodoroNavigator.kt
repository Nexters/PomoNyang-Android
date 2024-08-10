package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.time.PomodoroTimeSettingRoute
import kotlinx.serialization.Serializable

@Serializable
data object Pomodoro

@Serializable
data object PomodoroSetting

@Serializable
data class TimeSetting(
    val isFocusTime: Boolean,
    val minute: Long
)

fun NavGraphBuilder.pomodoroScreen(
    isNewUser: Boolean,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navHostController: NavHostController
) {
    navigation<Pomodoro>(
        startDestination = PomodoroSetting
    ) {
        composable<PomodoroSetting> {
            PomodoroRoute(
                isNewUser = isNewUser,
                onShowSnackbar = onShowSnackbar,
                navHostController = navHostController
            )
        }

        composable<TimeSetting> { backStackEntry ->
            val routeData = backStackEntry.toRoute<TimeSetting>()
            PomodoroTimeSettingRoute(
                initialSettingTime = routeData.minute.toInt(),
                isFocusTime = routeData.isFocusTime
            ) {
                navHostController.popBackStack()
            }
        }
    }
}
