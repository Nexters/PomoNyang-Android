package com.pomonyang.mohanyang.presentation.model.setting

import androidx.compose.runtime.Immutable
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import java.time.Duration

@Immutable
data class PomodoroCategoryModel(
    val categoryNo: Int,
    val title: String,
    val categoryType: PomodoroCategoryType,
    val focusTime: Int,
    val restTime: Int
)

fun PomodoroSettingEntity.toModel() = PomodoroCategoryModel(
    categoryNo = categoryNo,
    title = title,
    categoryType = PomodoroCategoryType.safeValueOf(title),
    focusTime = Duration.parse(focusTime).toMinutes().toInt(),
    restTime = Duration.parse(restTime).toMinutes().toInt()
)
