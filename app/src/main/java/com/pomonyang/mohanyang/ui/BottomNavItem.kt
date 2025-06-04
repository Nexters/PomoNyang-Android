package com.pomonyang.mohanyang.ui

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val route: Any,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int,
    val label: String,
)
