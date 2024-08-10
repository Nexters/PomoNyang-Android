package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pomonyang.mohanyang.presentation.designsystem.picker.MnWheelMinutePicker
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.PomodoroRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.SettingButton
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import kotlinx.collections.immutable.toPersistentList
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
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MnWheelMinutePicker(
                    items = (5..60 step 5).toPersistentList(),
                    initialItem = routeData.minute.toInt(),
                    halfDisplayCount = 3
                )

                SettingButton(
                    modifier = Modifier.padding(top = 40.dp),
                    backgroundColor = MnTheme.backgroundColorScheme.inverse
                ) {}
            }
        }
    }
}
