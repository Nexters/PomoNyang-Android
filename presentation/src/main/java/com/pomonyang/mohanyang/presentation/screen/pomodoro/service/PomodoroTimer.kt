package com.pomonyang.mohanyang.presentation.screen.pomodoro.service

internal interface PomodoroTimer {
    fun startTimer(maxTime: Int, eventHandler: PomodoroTimerEventHandler)
    fun stopTimer()
}
