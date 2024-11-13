package com.pomonyang.mohanyang.presentation.screen

import com.mohanyang.presentation.BuildConfig

object PomodoroConstants {
    val TIMER_DELAY = if (BuildConfig.DEBUG) 100L else 1_000L
    val MAX_EXCEEDED_TIME = if (BuildConfig.DEBUG) 60 else 3600
    const val MAX_FOCUS_MINUTES = 60
    const val MAX_REST_MINUTES = 30
    const val MIN_FOCUS_MINUTES = 10
    const val MIN_REST_MINUTES = 5
    const val ONE_SECOND = 1
    const val DEFAULT_TIME = "00:00"
    const val POMODORO_NOTIFICATION_CHANNEL_ID = "pomodoro_notification_channel_id"
    const val POMODORO_NOTIFICATION_CHANNEL_NAME = "pomodoro_notification_channel_name"
    const val POMODORO_NOTIFICATION_ID = 1
}
