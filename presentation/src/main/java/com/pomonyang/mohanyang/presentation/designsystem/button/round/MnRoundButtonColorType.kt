package com.pomonyang.mohanyang.presentation.designsystem.button.round

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnRoundButtonColorType {
    val primary: MnRoundButtonColors
        @Composable
        @ReadOnlyComposable
        get() = MnRoundButtonColors(
            containerColor = MnTheme.backgroundColorScheme.accent1,
            iconColor = MnTheme.iconColorScheme.inverse
        )
    val secondary: MnRoundButtonColors
        @Composable
        @ReadOnlyComposable
        get() = MnRoundButtonColors(
            containerColor = MnTheme.backgroundColorScheme.inverse,
            iconColor = MnTheme.iconColorScheme.tertiary
        )
}
