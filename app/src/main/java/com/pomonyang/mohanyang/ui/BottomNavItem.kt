package com.pomonyang.mohanyang.ui

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.util.MohanyangEventLog

data class BottomNavItem(
    val route: Any,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int,
    val label: String,
    val log: MohanyangEventLog,
)
