package com.pomonyang.mohanyang.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PomodoroTimerRequest(
    val clientFocusTimeId: String,
    val categoryNo: Int,
    val focusedTime: String,
    val restedTime: String,
    val doneAt: String,
)
