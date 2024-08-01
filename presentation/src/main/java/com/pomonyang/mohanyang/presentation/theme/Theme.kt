package com.pomonyang.mohanyang.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun MnTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMnIconColorScheme provides MnTheme.iconColorScheme,
        LocalMnBackgroundColorScheme provides MnTheme.backgroundColorScheme,
        LocalMnTextColorScheme provides MnTheme.textColorScheme,
        LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object MnTheme {
    val iconColorScheme: MnColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMnIconColorScheme.current

    val backgroundColorScheme: MnColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMnBackgroundColorScheme.current

    val textColorScheme: MnColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMnTextColorScheme.current

    val typography: MnTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMnTypo.current
}
