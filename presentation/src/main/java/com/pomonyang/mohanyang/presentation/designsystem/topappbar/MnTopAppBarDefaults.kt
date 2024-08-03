package com.pomonyang.mohanyang.presentation.designsystem.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pomonyang.mohanyang.presentation.designsystem.token.MnColor
import com.pomonyang.mohanyang.presentation.theme.MnTheme

object MnTopAppBarDefaults {
    val height = 56.dp
    val iconHorizontalPadding = 8.dp

    @Composable
    fun topAppBarColors(
        containerColor: Color = MnColor.Gray50,
        navigationIconContentColor: Color = MnTheme.iconColorScheme.primary,
        titleContentColor: Color = MnTheme.textColorScheme.primary,
        actionIconContentColor: Color = MnTheme.iconColorScheme.primary
    ) = MnAppBarColors(
        containerColor = containerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor
    )
}

@Immutable
data class MnAppBarColors(
    val containerColor: Color,
    val navigationIconContentColor: Color,
    val titleContentColor: Color,
    val actionIconContentColor: Color
)
