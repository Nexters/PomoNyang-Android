package com.pomonyang.mohanyang.presentation.model.category

import androidx.compose.runtime.Immutable
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon

@Immutable
data class PomodoroCategoryModel(
    val categoryNo: Int,
    val title: String,
    val categoryIcon: CategoryIcon,
)

fun PomodoroSettingEntity.toCategoryModel() = PomodoroCategoryModel(
    categoryNo = categoryNo,
    title = title,
    categoryIcon = CategoryIcon.valueOf(iconType)
)
