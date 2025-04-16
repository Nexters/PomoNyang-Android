package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pomonyang.mohanyang.presentation.screen.pomodoro.focus.PomodoroFocusRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting.PomodoroBreakReadyRoute
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data object Pomodoro

@Serializable
data object PomodoroFocusTimer

@Serializable
data class PomodoroRestWaiting(
    val pomodoroId: String,
    val type: String,
    val focusTime: Int,
    val exceededTime: Int,
)

@Serializable
data class PomodoroRest(
    val pomodoroId: String,
)

fun NavGraphBuilder.pomodoroScreen(
    onForceGoHome: () -> Unit,
    onShowSnackbar: (String, Int?) -> Unit,
    navHostController: NavHostController,
) {
    navigation<Pomodoro>(
        startDestination = PomodoroFocusTimer,
    ) {
        composable<PomodoroFocusTimer> {
            PomodoroFocusRoute(
                goToRest = { type, focusTime, exceededTime, pomodoroId ->
                    navHostController.navigate(
                        PomodoroRestWaiting(
                            type = type,
                            focusTime = focusTime,
                            exceededTime = exceededTime,
                            pomodoroId = pomodoroId,
                        ),
                    ) {
                        popUpTo<PomodoroFocusTimer> {
                            inclusive = true
                        }
                    }
                },
                goToHome = {
                    navHostController.popBackStack()
                },
            )
        }

        composable<PomodoroRestWaiting> {
            PomodoroBreakReadyRoute(
                goToHome = {
                    navHostController.popBackStack()
                },
                goToPomodoroRest = {
                    navHostController.navigate(PomodoroRest(it)) {
                        popUpTo<PomodoroRestWaiting> {
                            inclusive = true
                        }
                    }
                },
                forceGoHome = {
                    onForceGoHome()
                    navHostController.popBackStack()
                },
                onShowSnackbar = onShowSnackbar,
            )
        }

        composable<PomodoroRest> {
            PomodoroRestRoute(
                onShowSnackbar = onShowSnackbar,
                goToHome = {
                    navHostController.popBackStack()
                },
                goToFocus = {
                    navHostController.navigate(
                        PomodoroFocusTimer,
                    ) {
                        popUpTo<PomodoroRest> {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}
