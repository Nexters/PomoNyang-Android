package com.pomonyang.mohanyang.presentation.service.rest

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

internal class RestTimer @Inject constructor(
    private val timerRepository: PomodoroTimerRepository
) : PomodoroTimer {

    private var timer: Timer? = null
    private var timeElapsed = 0
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun startTimer(maxTime: Int, eventHandler: PomodoroTimerEventHandler) {
        Timber.tag("TIMER").d("startRest timer / maxTime : $maxTime")
        if (timer == null) {
            timeElapsed = 0
            timer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    timeElapsed += ONE_SECOND
                    timerRepository.incrementRestedTime()

                    Timber.tag("TIMER").d("countRestTime: $timeElapsed ")

                    if (timeElapsed >= maxTime) {
                        eventHandler.onTimeEnd()
                    } else if (timeElapsed >= maxTime + MAX_EXCEEDED_TIME) {
                        eventHandler.onTimeExceeded()
                        stopTimer()
                    }
                }
            }
        }
    }

    override fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}
