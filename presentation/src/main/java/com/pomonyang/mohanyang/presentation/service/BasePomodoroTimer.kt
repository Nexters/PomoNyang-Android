package com.pomonyang.mohanyang.presentation.service

import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import java.util.*
import kotlin.concurrent.fixedRateTimer
import timber.log.Timber

internal abstract class BasePomodoroTimer : PomodoroTimer {
    private var timer: Timer? = null
    private var remainingTime = 0

    protected abstract fun getTagName(): String

    override fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        category: PomodoroCategoryType?
    ) {
        Timber.tag(getTagName()).d("startTimer / maxTime : $maxTime")
        if (timer == null) {
            remainingTime = maxTime
            timer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                remainingTime -= ONE_SECOND

                Timber.tag(getTagName()).d("remainingTime: $remainingTime")

                val timeToDisplay = if (remainingTime >= 0) remainingTime else 0
                val overtime = if (remainingTime >= 0) 0 else -remainingTime

                eventHandler.updateTimer(
                    time = timeToDisplay.toString(),
                    overtime = overtime.toString(),
                    category = category
                )

                if (remainingTime == 0) {
                    eventHandler.onTimeEnd()
                } else if (remainingTime <= -MAX_EXCEEDED_TIME) {
                    eventHandler.onTimeExceeded()
                    stopTimer()
                }
            }
        }
    }

    override fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}
