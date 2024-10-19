package com.pomonyang.mohanyang.presentation.service

internal interface PomodoroTimerEventHandler {
    fun onTimeEnd()
    fun onTimeExceeded()
}
