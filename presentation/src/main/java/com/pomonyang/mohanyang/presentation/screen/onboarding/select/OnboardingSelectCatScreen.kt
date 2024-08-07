package com.pomonyang.mohanyang.presentation.screen.onboarding.select

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
fun OnboardingSelectCatRoute(
    onNamingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardingSelectCatScreen(
        onNamingClick = onNamingClick,
        modifier = modifier
    )
}

@Composable
fun OnboardingSelectCatScreen(
    onNamingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "select cat", fontSize = 30.sp)
            Button(onClick = onNamingClick) {
                Text(text = "go to naming")
            }
        }
    }
}
