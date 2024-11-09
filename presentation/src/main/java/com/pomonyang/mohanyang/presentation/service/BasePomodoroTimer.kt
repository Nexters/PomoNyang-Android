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
    private var timeElapsed = 0

    protected abstract fun getTagName(): String

    override fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        category: PomodoroCategoryType?
    ) {
        Timber.tag(getTagName()).d("startTimer / maxTime : $maxTime")
        if (timer == null) {
            timeElapsed = 0
            timer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                timeElapsed += ONE_SECOND

                Timber.tag(getTagName()).d("countTime: $timeElapsed ")

                eventHandler.updateTimer(
                    time = if (timeElapsed >= maxTime) maxTime.toString() else timeElapsed.toString(),
                    overtime = if (maxTime >= timeElapsed) "0" else (timeElapsed - maxTime).toString(),
                    category = category
                )

                if (timeElapsed == maxTime) {
                    eventHandler.onTimeEnd()
                } else if (timeElapsed >= maxTime + MAX_EXCEEDED_TIME) {
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
