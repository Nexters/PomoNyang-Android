package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryModel

internal interface PomodoroTimerEventHandler {
    fun onTimeEnd()
    fun onTimeExceeded()
    fun updateTimer(
        timerId: String,
        time: String,
        overtime: String,
        category: CategoryModel?,
    )
}
