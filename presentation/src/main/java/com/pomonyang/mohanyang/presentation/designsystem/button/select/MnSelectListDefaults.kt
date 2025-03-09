package com.pomonyang.mohanyang.presentation.designsystem.button.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.pomonyang.mohanyang.presentation.theme.MnTheme

@Immutable
data class MnSelectListColors(
    val enabledTextColor: Color,
    val disabledTextColor: Color,
    val enabledIconTint: Color,
    val disabledIconTint: Color,
)

object MnSelectListDefaults {

    @Composable
    fun colors(
        enabledTextColor: Color = MnTheme.textColorScheme.primary,
        disabledTextColor: Color = MnTheme.textColorScheme.disabled,
        enabledIconTint: Color =  Color.Unspecified,
        disabledIconTint: Color = MnTheme.textColorScheme.disabled,
    ) = MnSelectListColors(
        enabledTextColor = enabledTextColor,
        disabledTextColor = disabledTextColor,
        enabledIconTint = enabledIconTint,
        disabledIconTint = disabledIconTint,
    )
}
