package com.pomonyang.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.pomonyang.presentation.screen.home.Home
import com.pomonyang.presentation.screen.onboarding.OnBoarding
import com.pomonyang.ui.PomoNyangAppState

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
        OnBoarding
    } else {
        Home
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
    }
}
