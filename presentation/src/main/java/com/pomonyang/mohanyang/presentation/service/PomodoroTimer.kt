package com.pomonyang.mohanyang.presentation.service

internal interface PomodoroTimer {
    fun startTimer(maxTime: Int, eventHandler: PomodoroTimerEventHandler)
    fun stopTimer()
}
