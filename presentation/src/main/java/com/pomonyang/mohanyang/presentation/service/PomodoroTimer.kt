package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType

internal interface PomodoroTimer {

    fun startTimer(
        timerId: String,
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        category: PomodoroCategoryType? = null,
    )

    fun stopTimer()
}
