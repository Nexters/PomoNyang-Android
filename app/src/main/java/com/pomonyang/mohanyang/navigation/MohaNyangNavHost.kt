package com.pomonyang.mohanyang.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.mohanyang.presentation.screen.home.Home
import com.pomonyang.mohanyang.presentation.screen.home.homeScreen
import com.pomonyang.mohanyang.presentation.screen.onboarding.Onboarding
import com.pomonyang.mohanyang.presentation.screen.onboarding.onboardingScreen
import com.pomonyang.mohanyang.presentation.screen.pomodoro.setting.pomodoroSettingScreen
import com.pomonyang.mohanyang.ui.MohaNyangAppState

@Composable
internal fun MohaNyangNavHost(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    mohaNyangAppState: MohaNyangAppState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val navHostController = mohaNyangAppState.navHostController
    val navigateUp: () -> Unit = { navHostController.navigateUp() }

    val startDestination: Any = if (mohaNyangAppState.isNewUser) {
        Onboarding
    } else {
        Home
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardingScreen(
            navigateUp = navigateUp,
            navHostController = mohaNyangAppState.navHostController
        )

        homeScreen(
            navigateUp = navigateUp,
            navHostController = mohaNyangAppState.navHostController
        )

        pomodoroSettingScreen(isNewUser = mohaNyangAppState.isNewUser)
    }
}
