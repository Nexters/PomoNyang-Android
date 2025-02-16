package com.pomonyang.mohanyang.presentation.designsystem.tooltip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnTooltipDefaults {
    val anchorWidth = 14.dp
    val anchorHeight = 9.dp
    val overlayBackgroundColor = MnColor.Black.copy(alpha = 0.5f)

    @Composable
    fun lightTooltipColors(
        containerColor: Color = MnColor.White,
        contentColor: Color = MnTheme.textColorScheme.secondary,
    ) = MnTooltipColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )

    @Composable
    fun darkTooltipColors(
        containerColor: Color = MnTheme.backgroundColorScheme.inverse,
        contentColor: Color = MnTheme.textColorScheme.inverse,
    ) = MnTooltipColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

@Immutable
data class MnTooltipColors(
    val containerColor: Color,
    val contentColor: Color,
)
