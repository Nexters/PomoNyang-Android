package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Immutable
data class MnSelectButtonColors(
    val borderColor: Color,
    val containerColor: Color,
    val titleContentColor: Color,
    val subtitleContentColor: Color,
    val iconColor: Color
)

object MnSelectButtonSelector {
    val default: MnSelectButtonColors
        @Composable
        get() = MnSelectButtonColors(
            borderColor = Color.Unspecified,
            containerColor = MnTheme.backgroundColorScheme.secondary,
            titleContentColor = MnTheme.textColorScheme.secondary,
            subtitleContentColor = MnTheme.textColorScheme.tertiary,
            iconColor = MnTheme.iconColorScheme.tertiary
        )

    val selected: MnSelectButtonColors
        @Composable
        get() = MnSelectButtonColors(
            borderColor = MnTheme.backgroundColorScheme.accent1,
            containerColor = MnTheme.backgroundColorScheme.accent2,
            titleContentColor = MnTheme.textColorScheme.primary,
            subtitleContentColor = MnTheme.textColorScheme.tertiary,
            iconColor = MnTheme.iconColorScheme.tertiary

        )

    val disabled: MnSelectButtonColors
        @Composable
        get() = MnSelectButtonColors(
            borderColor = Color.Unspecified,
            containerColor = MnTheme.backgroundColorScheme.secondary,
            titleContentColor = MnTheme.textColorScheme.disabled,
            subtitleContentColor = MnTheme.textColorScheme.disabled,
            iconColor = MnTheme.iconColorScheme.disabled

        )
}
