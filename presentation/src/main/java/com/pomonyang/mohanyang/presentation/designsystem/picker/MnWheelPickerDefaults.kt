package com.pomonyang.mohanyang.presentation.designsystem.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnWheelPickerDefaults {
    val itemHeight: Dp = 98.dp

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

    @Composable
    fun styles() = MnWheelPickerStyles(
        selectedTextStyle = MnTheme.typography.header1,
        unSelectedTextStyle = MnTheme.typography.header2
    )
}

@Stable
data class MnWheelPickerColor(
    val fadeColor: Color,
    val selectedTextColor: Color,
    val unSelectedTextColor: Color
)

@Stable
data class MnWheelPickerStyles(
    val selectedTextStyle: TextStyle,
    val unSelectedTextStyle: TextStyle
)
