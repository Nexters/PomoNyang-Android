package com.pomonyang.mohanyang.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import com.pomonyang.mohanyang.presentation.service.focus.PomodoroFocusTimerService
import com.pomonyang.mohanyang.presentation.service.rest.PomodoroRestTimerService
import timber.log.Timber

internal fun Context.startFocusTimer(maxTime: Int) {
    Timber.tag("TIMER").d("startFocusTimer")
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

internal fun Context.startRestTimer(maxTime: Int) {
    Timber.tag("TIMER").d("startRestTimer")
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

internal fun Context.stopFocusTimer() {
    Timber.tag("TIMER").d("stopFocusTimer")
    startService(
        Intent(this, PomodoroFocusTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
        }
    )
}

internal fun Context.stopRestTimer() {
    Timber.tag("TIMER").d("stopRestTimer")
    startService(
        Intent(this, PomodoroRestTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
        }
    )
}
