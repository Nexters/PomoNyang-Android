package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PomodoroRestRoute(
    modifier: Modifier = Modifier
) {
    PomodoroRestScreen()
}

@Composable
fun PomodoroRestScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pomodoro Rest")
    }
}

@Preview
@Composable
private fun PomodoroRestScreenPreview() {
}
