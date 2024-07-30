package com.pomonyang.mohanyang.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun MohaNyangTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMohaNyangIconColorScheme provides MohaNyangTheme.iconColorScheme,
        LocalMohaNyangBackgroundColorScheme provides MohaNyangTheme.backgroundColorScheme,
        LocalMohaNyangTextColorScheme provides MohaNyangTheme.textColorScheme
    ) {
        MaterialTheme(
            typography = Typography,
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
}
