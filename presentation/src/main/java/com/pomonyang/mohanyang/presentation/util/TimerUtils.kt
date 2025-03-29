package com.pomonyang.mohanyang.presentation.util

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryModel
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import com.pomonyang.mohanyang.presentation.service.focus.PomodoroFocusTimerService
import com.pomonyang.mohanyang.presentation.service.rest.PomodoroRestTimerService
import timber.log.Timber

internal fun Context.startFocusTimer(
    maxTime: Int,
    categoryTitle: String,
    categoryIcon: CategoryIcon,
    timerId: String,
) {
    Timber.tag("TIMER").d("startFocusTimer $timerId / $maxTime / $categoryTitle / $categoryIcon")
    startService(
        Intent(this, PomodoroFocusTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtras(
                bundleOf(
                    PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME to maxTime,
                    PomodoroTimerServiceExtras.INTENT_CATEGORY to CategoryModel(
                        name = categoryTitle,
                        icon = categoryIcon,
                    ),
                    PomodoroTimerServiceExtras.INTENT_TIMER_ID to timerId,
                ),
            )
        },
    )
}

internal fun Context.startRestTimer(
    maxTime: Int,
    timerId: String,
) {
    Timber.tag("TIMER").d("startRestTimer $timerId / $maxTime")
    startService(
        Intent(this, PomodoroRestTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_START
            putExtras(
                bundleOf(
                    PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME to maxTime,
                    PomodoroTimerServiceExtras.INTENT_TIMER_ID to timerId,
                ),
            )
        },
    )
}

internal fun Context.stopFocusTimer(timerId: String) {
    Timber.tag("TIMER").d("stopFocusTimer $timerId")
    startService(
        Intent(this, PomodoroFocusTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
            putExtras(
                bundleOf(PomodoroTimerServiceExtras.INTENT_TIMER_ID to timerId),
            )
        },
    )
}

internal fun Context.stopRestTimer(timerId: String) {
    Timber.tag("TIMER").d("stopRestTimer $timerId")
    startService(
        Intent(this, PomodoroRestTimerService::class.java).apply {
            action = PomodoroTimerServiceExtras.ACTION_TIMER_STOP
            putExtras(
                bundleOf(PomodoroTimerServiceExtras.INTENT_TIMER_ID to timerId),
            )
        },
    )
}
