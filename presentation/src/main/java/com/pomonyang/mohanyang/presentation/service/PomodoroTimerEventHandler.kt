package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType

internal interface PomodoroTimerEventHandler {
    fun onTimeEnd()
    fun onTimeExceeded()
    fun updateTimer(
        timerId: String,
        time: String,
        overtime: String,
        category: PomodoroCategoryType?,
    )
}
