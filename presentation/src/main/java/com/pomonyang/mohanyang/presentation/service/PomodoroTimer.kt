package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager

internal interface PomodoroTimer {
    fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        pomodoroNotificationManager: PomodoroNotificationManager,
        category: PomodoroCategoryType? = null
    )
    fun stopTimer()
}
