package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSettingResponse(
    val no: Int,
    val title: String,
    val focusTime: String,
    val restTime: String
)
