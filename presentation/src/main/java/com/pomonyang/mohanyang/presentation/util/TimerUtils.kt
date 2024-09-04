package com.pomonyang.mohanyang.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf

fun Context.startTimer(isFocus: Boolean, maxTime: Int) {
    startService(
        Intent(this, PomodoroTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtras(
                bundleOf(
                    PomodoroTimerServiceExtras.INTENT_TIMER_IS_FOCUS to isFocus,
                    PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME to maxTime
                )
            )
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
