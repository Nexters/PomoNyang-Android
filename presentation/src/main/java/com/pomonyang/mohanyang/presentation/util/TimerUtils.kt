package com.pomonyang.mohanyang.presentation.util

import android.content.Context
import android.content.Intent

fun Context.startTimer(isFocus: Boolean) {
    startService(
        Intent(this, PomodoroTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtra(PomodoroTimerServiceExtras.INTENT_TIMER_IS_FOCUS, isFocus)
        }
    )
}

fun Context.stopTimer(isFocus: Boolean) {
    startService(
        Intent(this, PomodoroTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
            putExtra(PomodoroTimerServiceExtras.INTENT_TIMER_IS_FOCUS, isFocus)
        }
    )
}
