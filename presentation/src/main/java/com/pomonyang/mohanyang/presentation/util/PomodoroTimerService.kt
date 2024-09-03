package com.pomonyang.mohanyang.presentation.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.domain.usecase.InsertPomodoroInitialDataUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private enum class TimerKind {
    FOCUS,
    REST
}

@AndroidEntryPoint
class PomodoroTimerService : Service() {

    @Inject
    lateinit var pomodoroTimerRepository: PomodoroTimerRepository

    @Inject
    lateinit var pomodoroInitialDataUseCase: InsertPomodoroInitialDataUseCase

    private lateinit var timerKind: TimerKind
    private lateinit var timer: Timer

    private var scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val type = intent.getBooleanExtra(PomodoroTimerServiceExtras.INTENT_TIMER_KIND, true)
        val action = intent.action
        timerKind = when (type) {
            true -> {
                TimerKind.FOCUS
            }

            false -> {
                TimerKind.REST
            }
        }

        when (action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> startTimer()
            PomodoroTimerServiceExtras.ACTION_TIMER_STOP -> stopTimer()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {
        scope.launch {
            pomodoroInitialDataUseCase()
        }
        timer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
            scope.launch {
                when (timerKind) {
                    TimerKind.FOCUS -> {
                        pomodoroTimerRepository.incrementFocusedTime()
                    }

                    TimerKind.REST -> {
                        pomodoroTimerRepository.incrementRestedTime()
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
    }

    companion object {
        private const val TIMER_DELAY = 1000L
    }
}
