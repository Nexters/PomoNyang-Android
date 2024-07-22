package com.pomonyang.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.presentation.screen.home.Home
import com.pomonyang.presentation.screen.home.homeScreen
import com.pomonyang.presentation.screen.onboarding.Onboarding
import com.pomonyang.presentation.screen.onboarding.onboardingScreen
import com.pomonyang.ui.PomoNyangAppState
import kotlinx.coroutines.launch

@Composable
internal fun PomoNyangNavHost(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    pomoNyangAppState: PomoNyangAppState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val navHostController = pomoNyangAppState.navHostController
    val navigateUp: () -> Unit = { navHostController.navigateUp() }

    val startDestination: Any = if (pomoNyangAppState.isFirstUser) {
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
