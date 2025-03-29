package com.pomonyang.mohanyang.presentation.model.setting

import androidx.compose.runtime.Immutable
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import java.time.Duration

@Immutable
data class PomodoroSettingModel(
    val categoryNo: Int,
    val title: String,
    val categoryType: CategoryIcon,
    val focusTime: Int,
    val restTime: Int,
    val isSelected: Boolean,
)

fun PomodoroSettingEntity.toModel() = PomodoroSettingModel(
    categoryNo = categoryNo,
    title = title,
    categoryType = CategoryIcon.safeValueOf(title),
    focusTime = Duration.parse(focusTime).toMinutes().toInt(),
    restTime = Duration.parse(restTime).toMinutes().toInt(),
    isSelected = isSelected,
)
