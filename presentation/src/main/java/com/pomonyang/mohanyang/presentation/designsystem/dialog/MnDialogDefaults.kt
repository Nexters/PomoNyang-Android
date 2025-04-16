package com.pomonyang.mohanyang.presentation.designsystem.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnDialogDefaults {
    val containerWidth = 335.dp

    @Composable
    fun colors(
        containerColor: Color = MnColor.White,
        positiveButtonContainerColor: Color = MnTheme.backgroundColorScheme.inverse,
        negativeButtonContainerColor: Color = MnTheme.backgroundColorScheme.accent1,
        titleColor: Color = MnTheme.textColorScheme.primary,
        subTitleColor: Color = MnTheme.textColorScheme.secondary,
        positiveButtonTextColor: Color = MnTheme.textColorScheme.inverse,
        negativeButtonTextColor: Color = MnTheme.textColorScheme.inverse,
    ) = MnDialogColors(
        containerColor = containerColor,
        positiveButtonContainerColor = positiveButtonContainerColor,
        negativeButtonContainerColor = negativeButtonContainerColor,
        titleColor = titleColor,
        subTitleColor = subTitleColor,
        positiveButtonTextColor = positiveButtonTextColor,
        negativeButtonTextColor = negativeButtonTextColor,
    )

    @Composable
    fun textStyles(
        titleTextStyle: TextStyle = MnTheme.typography.header4,
        subTitleTextStyle: TextStyle = MnTheme.typography.subBodyRegular,
        positiveButtonTextStyle: TextStyle = MnTheme.typography.bodySemiBold,
        negativeButtonTextStyle: TextStyle = MnTheme.typography.bodySemiBold,
    ) = MnDialogTextStyles(
        titleTextStyle = titleTextStyle,
        subTitleTextStyle = subTitleTextStyle,
        positiveButtonTextStyle = positiveButtonTextStyle,
        negativeButtonTextStyle = negativeButtonTextStyle,
    )
}

@Immutable
data class MnDialogColors(
    val containerColor: Color,
    val positiveButtonContainerColor: Color,
    val negativeButtonContainerColor: Color,
    val titleColor: Color,
    val subTitleColor: Color,
    val positiveButtonTextColor: Color,
    val negativeButtonTextColor: Color,
)

@Immutable
data class MnDialogTextStyles(
    val titleTextStyle: TextStyle,
    val subTitleTextStyle: TextStyle,
    val positiveButtonTextStyle: TextStyle,
    val negativeButtonTextStyle: TextStyle,
)
