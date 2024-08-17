package com.pomonyang.mohanyang.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
    onShowSnackbar: (String, Int?) -> Unit,
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier
) {
    val navHostController = mohaNyangAppState.navHostController
    val navigateUp: () -> Unit = { navHostController.navigateUp() }

    val startDestination: Any = if (mohaNyangAppState.isNewUser) {
        Onboarding
    } else {
        Pomodoro(false)
    }

    val slideDuration = 300

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier,
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(slideDuration)
            )
        },

        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(slideDuration)
            )
        }

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
