package com.pomonyang.mohanyang.presentation.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PomodoroTimerService : Service() {

    @Inject
    lateinit var pomodoroTimerRepository: PomodoroTimerRepository

    private var focusTimer: Timer? = null
    private var restTimer: Timer? = null

    private var scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isFocus = intent.getBooleanExtra(PomodoroTimerServiceExtras.INTENT_TIMER_IS_FOCUS, true)
        val action = intent.action

        Timber.d("[지훈] ${object {}.javaClass.enclosingMethod?.name} isFocus $isFocus / $action")

        when (action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                if (isFocus) {
                    startFocusTimer()
                } else {
                    startRestTimer()
                }
            }

            PomodoroTimerServiceExtras.ACTION_TIMER_STOP -> {
                if (isFocus) {
                    stopFocusTimer()
                } else {
                    stopRestTimer()
                }
            }
        }

        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        stopFocusTimer()
        stopRestTimer()
        return super.stopService(name)
    }

    private fun startFocusTimer() {
        if (focusTimer == null) {
            focusTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    Timber.d("[지훈] Focus 타이머 작동 중 ${this@fixedRateTimer.scheduledExecutionTime()}")
                    pomodoroTimerRepository.incrementFocusedTime()
                }
            }
        }
    }

    private fun stopFocusTimer() {
        focusTimer?.cancel()
        focusTimer = null
        Timber.d("[지훈] Focus 타이머 중지")
    }

    private fun startRestTimer() {
        if (restTimer == null) {
            restTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    Timber.d("[지훈] Rest 타이머 작동 중 ${this@fixedRateTimer.scheduledExecutionTime()}")
                    pomodoroTimerRepository.incrementRestedTime()
                }
            }
        }
    }

    private fun stopRestTimer() {
        restTimer?.cancel()
        restTimer = null
        Timber.d("[지훈] Rest 타이머 중지")
    }
}
