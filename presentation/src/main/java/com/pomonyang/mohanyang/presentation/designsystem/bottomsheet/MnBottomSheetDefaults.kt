package com.pomonyang.mohanyang.presentation.designsystem.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnBottomSheetDefaults {
    @Composable
    fun colors(
        containerColor: Color = MnColor.White,
        titleColor: Color = MnTheme.textColorScheme.primary,
        subTitleColor: Color = MnTheme.textColorScheme.secondary
    ) = MnBottomSheetColors(
        containerColor = containerColor,
        titleColor = titleColor,
        subTitleColor = subTitleColor
    )

    @Composable
    fun textStyles(
        titleTextStyle: TextStyle = MnTheme.typography.header3,
        subTitleTextStyle: TextStyle = MnTheme.typography.subBodyRegular
    ) = MnBottomSheetTextStyles(
        titleTextStyle = titleTextStyle,
        subTitleTextStyle = subTitleTextStyle
    )
}

@Immutable
data class MnBottomSheetColors(
    val containerColor: Color,
    val titleColor: Color,
    val subTitleColor: Color
)

@Immutable
data class MnBottomSheetTextStyles(
    val titleTextStyle: TextStyle,
    val subTitleTextStyle: TextStyle
)
