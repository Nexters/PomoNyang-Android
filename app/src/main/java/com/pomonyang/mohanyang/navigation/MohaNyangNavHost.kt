package com.pomonyang.mohanyang.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.mohanyang.presentation.screen.onboarding.Onboarding
import com.pomonyang.mohanyang.presentation.screen.onboarding.onboardingScreen
import com.pomonyang.mohanyang.presentation.screen.pomodoro.Pomodoro
import com.pomonyang.mohanyang.presentation.screen.pomodoro.pomodoroScreen
import com.pomonyang.mohanyang.ui.MohaNyangAppState

@Composable
internal fun MohaNyangNavHost(
    onShowSnackbar: (String, String?) -> Unit,
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier
) {
    val navHostController = mohaNyangAppState.navHostController
    val navigateUp: () -> Unit = { navHostController.navigateUp() }

    val startDestination: Any = if (mohaNyangAppState.isNewUser) {
        Onboarding
    } else {
        Pomodoro
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardingScreen(
            navHostController = mohaNyangAppState.navHostController
        )

        pomodoroScreen(
            isNewUser = mohaNyangAppState.isNewUser,
            navHostController = mohaNyangAppState.navHostController,
            onShowSnackbar = onShowSnackbar
        )
    }
}
