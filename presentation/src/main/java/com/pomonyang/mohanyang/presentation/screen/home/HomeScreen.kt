package com.pomonyang.mohanyang.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
internal fun HomeRoute(
    onNavigationClick: () -> Unit,
    onPomodoroSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreen(
        modifier = modifier,
        onNavigationClick = onNavigationClick,
        onPomodoroSettingClick = onPomodoroSettingClick
    )
}

@Composable
private fun HomeScreen(
    onNavigationClick: () -> Unit,
    onPomodoroSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onNavigationClick) {
            Text(text = "back")
        }
        Button(onPomodoroSettingClick) {
            Text(text = "go pomodoro setting")
        }
    }
}

@ThemePreviews
@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    MnTheme {
        HomeScreen(
            onPomodoroSettingClick = {},
            onNavigationClick = {}
        )
    }
}
