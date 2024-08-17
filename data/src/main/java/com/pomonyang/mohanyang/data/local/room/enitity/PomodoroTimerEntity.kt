package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pomodoro_timer")
data class PomodoroTimerEntity(
    @PrimaryKey
    val focusTimeId: String = UUID.randomUUID().toString(),
    val focusedTime: String = "PT0M",
    val restedTime: String = "PT0M",
    val categoryNo: Int,
    val doneAt: String
)
