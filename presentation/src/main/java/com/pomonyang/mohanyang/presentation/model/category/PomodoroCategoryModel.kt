package com.pomonyang.mohanyang.presentation.model.category

import androidx.compose.runtime.Immutable
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType

@Immutable
data class PomodoroCategoryModel(
    val categoryNo: Int,
    val title: String,
    val categoryType: PomodoroCategoryType,
)

fun PomodoroSettingEntity.toCategoryModel() = PomodoroCategoryModel(
    categoryNo = categoryNo,
    title = title,
    categoryType = PomodoroCategoryType.safeValueOf(title),
)
