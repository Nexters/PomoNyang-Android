package com.pomonyang.mohanyang.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun MohaNyangTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMohaNyangIconColorScheme provides MohaNyangTheme.iconColorScheme,
        LocalMohaNyangBackgroundColorScheme provides MohaNyangTheme.backgroundColorScheme,
        LocalMohaNyangTextColorScheme provides MohaNyangTheme.textColorScheme,
        LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object MohaNyangTheme {
    val iconColorScheme: MohaNyangColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMohaNyangIconColorScheme.current

    val backgroundColorScheme: MohaNyangColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMohaNyangBackgroundColorScheme.current

    val textColorScheme: MohaNyangColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMohaNyangTextColorScheme.current

    val typography: MohaNyangTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMohaNyangTypo.current
}
