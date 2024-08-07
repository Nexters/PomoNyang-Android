package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingNamingCatRoute(
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingNamingCatScreen(
        onHomeClick = onHomeClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingNamingCatScreen(
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "naming cat", fontSize = 30.sp)
            Button(onClick = onHomeClick) {
                Text(text = "start pomodoro")
            }
        }
    }
}
