package com.pomonyang.mohanyang.data.local.room.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pomonyang.mohanyang.data.local.room.util.formatDurationToMinutesString
import com.pomonyang.mohanyang.data.remote.model.request.PomodoroTimerRequest
import com.pomonyang.mohanyang.data.repository.util.getCurrentIsoInstant

@Entity(tableName = "pomodoro_timer")
data class PomodoroTimerEntity(
    @PrimaryKey
    val focusTimeId: String,
    val focusedTime: Int = 0,
    val restedTime: Int = 0,
    val doneAt: String = "",
    val categoryNo: Int,
)

fun PomodoroTimerEntity.toRequestModel() = PomodoroTimerRequest(
    clientFocusTimeId = focusTimeId,
    categoryNo = categoryNo,
    focusedTime = focusedTime.formatDurationToMinutesString(),
    restedTime = restedTime.formatDurationToMinutesString(),
    doneAt = doneAt.ifEmpty { getCurrentIsoInstant() },
)
