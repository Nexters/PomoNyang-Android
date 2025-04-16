package com.pomonyang.mohanyang.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.mohanyang.presentation.screen.home.Home
import com.pomonyang.mohanyang.presentation.screen.home.homeScreen
import com.pomonyang.mohanyang.presentation.screen.mypage.myPageScreen
import com.pomonyang.mohanyang.presentation.screen.onboarding.Onboarding
import com.pomonyang.mohanyang.presentation.screen.onboarding.onboardingScreen
import com.pomonyang.mohanyang.presentation.screen.pomodoro.pomodoroScreen
import com.pomonyang.mohanyang.ui.MohaNyangAppState

@Composable
internal fun MohaNyangNavHost(
    onShowSnackbar: (String, Int?) -> Unit,
    onForceGoHome: () -> Unit,
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier,
) {
    val navHostController = mohaNyangAppState.navHostController
    val navigateUp: () -> Unit = { navHostController.navigateUp() }

    val startDestination: Any = if (mohaNyangAppState.isNewUser) {
        Onboarding
    } else {
        Home
    }

    val slideDuration = 500

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier,
        exitTransition = {
            ExitTransition.None
        },

        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(slideDuration),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(slideDuration),
            )
        },

        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(slideDuration),
            )
        },

    ) {
        onboardingScreen(
            navHostController = mohaNyangAppState.navHostController,
            onShowSnackbar = onShowSnackbar,
        )

        homeScreen(
            isNewUser = mohaNyangAppState.isNewUser,
            navHostController = mohaNyangAppState.navHostController,
            onShowSnackbar = onShowSnackbar,
        )

        pomodoroScreen(
            onForceGoHome = onForceGoHome,
            onShowSnackbar = onShowSnackbar,
            navHostController = mohaNyangAppState.navHostController,
        )

        myPageScreen(
            navHostController = navHostController,
            isOfflineState = mohaNyangAppState.isOffline,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
