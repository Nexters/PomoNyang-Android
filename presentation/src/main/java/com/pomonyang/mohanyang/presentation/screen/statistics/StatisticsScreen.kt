package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews

@Composable
fun StatisticsRoute(
    onShowSnackbar: (String, Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    StatisticsScreen(modifier)
}

@Composable
private fun StatisticsScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MnTheme.backgroundColorScheme.primary,
    ) {
        Text("StatisticsScreen")
    }
}

@ThemePreviews
@Composable
private fun StatisticsScreenPreview() {
    MnTheme {
        StatisticsScreen()
    }
}
