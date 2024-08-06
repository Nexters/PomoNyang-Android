package com.pomonyang.mohanyang.presentation.designsystem.button.box

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MnBoxButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val iconColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledIconColor: Color
)
