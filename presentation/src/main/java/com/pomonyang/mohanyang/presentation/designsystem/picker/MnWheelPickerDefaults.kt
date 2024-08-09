package com.pomonyang.mohanyang.presentation.designsystem.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnWheelPickerDefaults {
    val itemHeight: Dp = 80.dp

    @Composable
    fun colors(
        fadeColor: Color = MnTheme.backgroundColorScheme.inverse,
        textColor: Color = MnTheme.textColorScheme.primary
    ) = MnWheelPickerColor(
        fadeColor = fadeColor,
        textColor = textColor
    )
}

@Stable
data class MnWheelPickerColor(
    val fadeColor: Color,
    val textColor: Color
)
