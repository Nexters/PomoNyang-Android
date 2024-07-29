package com.pomonyang.mohanyang.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pomonyang.mohanyang.presentation.util.DevicePreviews
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.debounceNoRippleClickable

@Composable
internal fun HomeRoute(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreen(
        modifier = modifier,
        onNavigationClick = onNavigationClick
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "back",
            modifier = Modifier.debounceNoRippleClickable { onNavigationClick() }
        )
    }
}

@ThemePreviews
@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    // TODO [코니] set theme
    HomeScreen {}
}
