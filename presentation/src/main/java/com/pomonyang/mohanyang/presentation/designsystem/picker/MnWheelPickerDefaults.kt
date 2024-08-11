package com.pomonyang.mohanyang.presentation.designsystem.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnWheelPickerDefaults {
    val focusItemHeight: Dp = 100.dp
    val itemHeight: Dp = 50.dp

    @Composable
    fun colors(
        fadeColor: Color = MnTheme.backgroundColorScheme.inverse,
        selectedTextColor: Color = MnTheme.textColorScheme.primary,
        unSelectedTextColor: Color = MnTheme.textColorScheme.disabled
    ) = MnWheelPickerColor(
        fadeColor = fadeColor,
        selectedTextColor = selectedTextColor,
        unSelectedTextColor = unSelectedTextColor
    )
}

@Stable
data class MnWheelPickerColor(
    val fadeColor: Color,
    val selectedTextColor: Color,
    val unSelectedTextColor: Color
)
