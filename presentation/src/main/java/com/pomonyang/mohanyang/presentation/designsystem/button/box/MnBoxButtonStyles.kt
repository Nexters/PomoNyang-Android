package com.pomonyang.mohanyang.presentation.designsystem.button.box

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnButtonStyleProperties
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnBoxButtonHeight {
    val large = 60.dp
    val medium = 52.dp
    val small = 40.dp
}

object MnBoxButtonStyles {
    val large: MnButtonStyleProperties
        @Composable
        get() = MnButtonStyleProperties(
            height = MnBoxButtonHeight.large,
            shape = RoundedCornerShape(MnRadius.small),
            contentPadding = PaddingValues(horizontal = MnSpacing.xLarge, vertical = MnSpacing.xLarge),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.header5,
        )
    val medium: MnButtonStyleProperties
        @Composable
        get() = MnButtonStyleProperties(
            height = MnBoxButtonHeight.medium,
            shape = RoundedCornerShape(MnRadius.small),
            contentPadding = PaddingValues(horizontal = MnSpacing.large, vertical = MnSpacing.large),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.bodySemiBold,
        )

    val small: MnButtonStyleProperties
        @Composable
        get() = MnButtonStyleProperties(
            height = MnBoxButtonHeight.small,
            shape = RoundedCornerShape(MnRadius.xSmall),
            contentPadding = PaddingValues(horizontal = MnSpacing.medium, vertical = MnSpacing.small),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.subBodySemiBold,
        )
}
