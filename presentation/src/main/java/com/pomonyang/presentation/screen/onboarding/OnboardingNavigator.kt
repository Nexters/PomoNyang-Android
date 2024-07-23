package com.pomonyang.presentation.screen.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

fun NavGraphBuilder.onboardingScreen(
    navigateUp: () -> Unit,
    onHomeClick: () -> Unit
) {
    composable<Onboarding> {
        OnboardingRoute(
            onNavigateUp = navigateUp,
            onHomeClick = onHomeClick
        )
    }
}
