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
data object PomodoroSetting

@Serializable
data class TimeSetting(
    val isFocusTime: Boolean,
    val type: String,
    val focusMinute: Long,
    val restMinute: Long,
    val categoryNo: Int
)

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
                navHostController = navHostController
            )
        }

        composable<TimeSetting> { backStackEntry ->
            val routeData = backStackEntry.toRoute<TimeSetting>()
            PomodoroTimeSettingRoute(
                initialFocusTime = routeData.focusMinute.toInt(),
                initialRestTime = routeData.restMinute.toInt(),
                type = routeData.type,
                categoryNo = routeData.categoryNo,
                isFocusTime = routeData.isFocusTime,
                onShowSnackbar = onShowSnackbar
            ) {
                navHostController.popBackStack()
            }
        }
    }
}
