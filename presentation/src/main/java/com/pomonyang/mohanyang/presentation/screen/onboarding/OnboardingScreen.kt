package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
internal fun OnboardingRoute(
    onNavigateUp: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingScreen(
        modifier = modifier,
        onHomeClick = onHomeClick
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onHomeClick) {
            Text(text = "go to home")
        }
    }
}

@ThemePreviews
@DevicePreviews
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen {}
}
