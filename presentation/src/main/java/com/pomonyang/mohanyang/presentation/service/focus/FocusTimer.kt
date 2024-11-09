package com.pomonyang.mohanyang.presentation.service.focus

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

internal class FocusTimer @Inject constructor(
    private val timerRepository: PomodoroTimerRepository
) : PomodoroTimer {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var timer: Timer? = null
    private var timeElapsed = 0

    override fun startTimer(
        maxTime: Int,
        eventHandler: PomodoroTimerEventHandler,
        pomodoroNotificationManager: PomodoroNotificationManager,
        category: PomodoroCategoryType?
    ) {
        Timber.tag("TIMER").d("startFocus timer / maxTime : $maxTime")
        if (timer == null) {
            timeElapsed = 0
            timer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    timeElapsed += ONE_SECOND
                    timerRepository.incrementFocusedTime()

                    Timber.tag("TIMER").d("countFocusTime: $timeElapsed ")

                    pomodoroNotificationManager.updateNotification(
                        time = if (timeElapsed >= maxTime) maxTime.toString() else timeElapsed.toString(),
                        overtime = if (maxTime >= timeElapsed) "0" else (timeElapsed - maxTime).toString(),
                        category = category
                    )

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
