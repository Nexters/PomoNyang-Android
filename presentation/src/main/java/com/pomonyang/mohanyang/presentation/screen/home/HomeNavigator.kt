package com.pomonyang.mohanyang.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.homeScreen(navigateUp: () -> Unit) {
    composable<Home> {
        HomeRoute(
            onNavigationClick = navigateUp
        )
    }
}
