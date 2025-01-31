package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pomonyang.mohanyang.presentation.screen.pomodoro.focus.PomodoroFocusRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.rest.PomodoroRestRoute
import com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting.PomodoroBreakReadyRoute
import kotlinx.serialization.Serializable

@Serializable
data object Pomodoro

@Serializable
internal data object PomodoroFocusTimer

@Serializable
internal data class PomodoroRestWaiting(
    val type: String,
    val focusTime: Int,
    val exceededTime: Int,
)

@Serializable
internal data object PomodoroRest

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
                pomodoroTimerViewModel = it.sharedViewModel(navHostController),
                goToRest = { type, focusTime, exceededTime ->
                    navHostController.navigate(
                        PomodoroRestWaiting(
                            type = type,
                            focusTime = focusTime,
                            exceededTime = exceededTime,
                        ),
                    ) {
                        popUpTo(PomodoroFocusTimer) { inclusive = true }
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
                    navHostController.navigate(PomodoroRest) {
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
                pomodoroTimerViewModel = it.sharedViewModel(navHostController),
                onShowSnackbar = onShowSnackbar,
                goToHome = {
                    navHostController.popBackStack()
                },
                goToFocus = {
                    navHostController.navigate(Pomodoro) {
                        popUpTo(PomodoroRest) { inclusive = true }
                    }
                },
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navHostController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navHostController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
