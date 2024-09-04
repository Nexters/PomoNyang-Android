package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_timer")
data class PomodoroTimerEntity(
    @PrimaryKey
    val focusTimeId: String,
    val focusedTime: Int = 0,
    val restedTime: Int = 0,
    val doneAt: String = "",
    val categoryNo: Int
)
