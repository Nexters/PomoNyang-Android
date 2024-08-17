package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_setting")
data class PomodoroSettingEntity(
    @PrimaryKey
    val categoryNo: Int,
    val title: String,
    val focusTime: String,
    val restTime: String
)
