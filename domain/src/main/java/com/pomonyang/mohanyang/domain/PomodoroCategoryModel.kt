package com.pomonyang.mohanyang.domain

import androidx.compose.runtime.Immutable
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import java.time.Duration

@Immutable
data class PomodoroCategoryModel(
    val no: Int,
    val title: String,
    val focusTime: Long,
    val restTime: Long
)

internal fun PomodoroSettingResponse.toModel() = PomodoroCategoryModel(
    no = no,
    title = title,
    focusTime = Duration.parse(focusTime).toMinutes(),
    restTime = Duration.parse(restTime).toMinutes()
)
