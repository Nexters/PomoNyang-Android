package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager

internal interface PomodoroTimer {
    fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        pomodoroNotificationManager: PomodoroNotificationManager
    )
    fun stopTimer()
}
