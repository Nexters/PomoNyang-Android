package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryModel

internal interface PomodoroTimer {

    fun startTimer(
        timerId: String,
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        category: CategoryModel? = null,
    )

    fun stopTimer()
}
