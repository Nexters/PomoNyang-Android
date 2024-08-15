package com.pomonyang.mohanyang.presentation.screen

import com.mohanyang.presentation.BuildConfig

object PomodoroConstants {
    val TIMER_DELAY = if (BuildConfig.DEBUG) 10L else 1_000L
    val MAX_EXCEEDED_TIME = if (BuildConfig.DEBUG) 600 else 3600
    const val MAX_FOCUS_TIME = 3600
    const val MIN_FOCUS_TIME = 300
    const val ENDED_FOCUS_TIME = 0
    const val ONE_SECOND = 1
    const val DEFAULT_TIME = "00:00"
}
