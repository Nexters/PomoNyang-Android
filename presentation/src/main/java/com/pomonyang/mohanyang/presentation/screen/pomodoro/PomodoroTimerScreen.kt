package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.topappbar.MnTopAppBar
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Composable
fun PomodoroTimerRoute(
    modifier: Modifier = Modifier
) {
}

@Composable
private fun PomodoroTimerScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MnTopAppBar(
            navigationIcon = {
            }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Preview
@Composable
private fun PomodoroTimerScreenPreview() {
    MnTheme {
        PomodoroTimerScreen()
    }
}
