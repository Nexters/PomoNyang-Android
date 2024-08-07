package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse

@Entity(tableName = "pomodoro_setting")
data class PomodoroSettingEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryNo: Int = 0,
    val title: String,
    val focusTime: String,
    val restTime: String
)

internal fun PomodoroSettingEntity.toResponse() = PomodoroSettingResponse(
    no = categoryNo,
    title = title,
    focusTime = focusTime,
    restTime = restTime
)
