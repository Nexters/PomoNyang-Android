package com.pomonyang.mohanyang.presentation.designsystem.button.box

import androidx.compose.runtime.Composable
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnBoxButtonColorType {
    val primary: MnBoxButtonColors
        @Composable
        get() = MnBoxButtonColors(
            containerColor = MnTheme.backgroundColorScheme.accent1,
            contentColor = MnTheme.textColorScheme.inverse,
            iconColor = MnTheme.iconColorScheme.inverse,
            disabledContainerColor = MnTheme.backgroundColorScheme.secondary,
            disabledContentColor = MnTheme.textColorScheme.disabled,
            disabledIconColor = MnTheme.iconColorScheme.disabled
        )

    val secondary: MnBoxButtonColors
        @Composable
        get() = MnBoxButtonColors(
            containerColor = MnTheme.backgroundColorScheme.inverse,
            contentColor = MnTheme.textColorScheme.inverse,
            iconColor = MnTheme.iconColorScheme.inverse,
            disabledContainerColor = MnTheme.backgroundColorScheme.inverse,
            disabledContentColor = MnTheme.textColorScheme.inverse,
            disabledIconColor = MnTheme.iconColorScheme.inverse
        )

    val tertiary: MnBoxButtonColors
        @Composable
        get() = MnBoxButtonColors(
            containerColor = MnTheme.backgroundColorScheme.secondary,
            contentColor = MnTheme.textColorScheme.tertiary,
            iconColor = MnTheme.iconColorScheme.tertiary,
            disabledContainerColor = MnTheme.backgroundColorScheme.secondary,
            disabledContentColor = MnTheme.textColorScheme.tertiary,
            disabledIconColor = MnTheme.iconColorScheme.tertiary
        )
}
