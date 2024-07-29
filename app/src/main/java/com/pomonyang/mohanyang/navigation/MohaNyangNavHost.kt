package com.pomonyang.mohanyang.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.mohanyang.presentation.screen.home.Home
import com.pomonyang.mohanyang.presentation.screen.home.homeScreen
import com.pomonyang.mohanyang.presentation.screen.onboarding.Onboarding
import com.pomonyang.mohanyang.presentation.screen.onboarding.onboardingScreen
import com.pomonyang.mohanyang.ui.MohaNyangAppState
import kotlinx.coroutines.launch

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
        onboardingScreen(navigateUp = navigateUp) {
            coroutineScope.launch { onShowSnackbar("arrived at the home screen.", null) }
            navHostController.navigate(Home)
        }
        homeScreen(navigateUp = navigateUp)
    }
}
