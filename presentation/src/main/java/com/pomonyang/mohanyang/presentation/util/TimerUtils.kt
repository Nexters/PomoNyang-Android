package com.pomonyang.mohanyang.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import com.pomonyang.mohanyang.presentation.service.focus.PomodoroFocusTimerService
import com.pomonyang.mohanyang.presentation.service.rest.PomodoroRestTimerService

fun Context.startFocusTimer(maxTime: Int) {
    startService(
        Intent(this, PomodoroFocusTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtras(
                bundleOf(
                    PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME to maxTime
                )
            )
        }
    )
}

fun Context.startRestTimer(maxTime: Int) {
    startService(
        Intent(this, PomodoroRestTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtras(
                bundleOf(
                    PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME to maxTime
                )
            )
        }
    )
}

fun Context.stopFocusTimer() {
    startService(
        Intent(this, PomodoroFocusTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
        }
    )
}

fun Context.stopRestTimer() {
    startService(
        Intent(this, PomodoroRestTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
        }
    )
}
