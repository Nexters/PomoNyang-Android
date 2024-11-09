package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType

internal interface PomodoroTimer {

    fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        category: PomodoroCategoryType? = null
    )

    fun stopTimer()
}
