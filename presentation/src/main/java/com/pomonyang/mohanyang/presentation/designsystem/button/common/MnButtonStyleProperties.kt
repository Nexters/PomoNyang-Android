package com.pomonyang.mohanyang.presentation.designsystem.button.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Immutable
data class MnButtonStyleProperties(
    val height: Dp,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val spacing: Dp,
    val textStyle: TextStyle
)
