package com.pomonyang.mohanyang.domain

import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import java.time.Duration

data class PomodoroCategoryModel(
    val no: Int,
    val title: String,
    val focusTime: Long,
    val restTime: Long
)

fun PomodoroSettingResponse.toModel() = PomodoroCategoryModel(
    no = no,
    title = title,
    focusTime = Duration.parse(focusTime).toHours(),
    restTime = Duration.parse(restTime).toHours()
)
