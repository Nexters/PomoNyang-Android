package com.pomonyang.mohanyang.presentation.screen.pomodoro.service

internal interface PomodoroTimerEventHandler {
    fun onTimeEnd()
    fun onTimeExceeded()
}
