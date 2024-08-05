package com.pomonyang.mohanyang.presentation.designsystem.button.text

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.button.common.MnButtonStyleProperties
import com.pomonyang.mohanyang.presentation.designsystem.token.MnRadius
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnTextButtonHeight {
    val large = 52.dp
    val medium = 40.dp
    val small = 40.dp
}

object MnTextButtonStyles {
    val large: MnButtonStyleProperties
        @Composable
        @ReadOnlyComposable
        get() = MnButtonStyleProperties(
            height = MnTextButtonHeight.large,
            shape = RoundedCornerShape(MnRadius.small),
            contentPadding = PaddingValues(horizontal = MnSpacing.xLarge),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.header5
        )
    val medium: MnButtonStyleProperties
        @Composable
        @ReadOnlyComposable
        get() = MnButtonStyleProperties(
            height = MnTextButtonHeight.medium,
            shape = RoundedCornerShape(MnRadius.small),
            contentPadding = PaddingValues(horizontal = MnSpacing.large),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.bodySemiBold
        )

    val small: MnButtonStyleProperties
        @Composable
        @ReadOnlyComposable
        get() = MnButtonStyleProperties(
            height = MnTextButtonHeight.small,
            shape = RoundedCornerShape(MnRadius.xSmall),
            contentPadding = PaddingValues(horizontal = MnSpacing.medium),
            spacing = MnSpacing.small,
            textStyle = MnTheme.typography.subBodySemiBold
        )
}
