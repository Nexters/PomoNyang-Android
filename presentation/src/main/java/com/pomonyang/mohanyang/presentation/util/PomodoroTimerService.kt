package com.pomonyang.mohanyang.presentation.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
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

    private var focusTimeElapsed = 0 // 초 단위로 경과된 시간 저장
    private var restTimeElapsed = 0 // 초 단위로 경과된 시간 저장

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isFocus = intent.getBooleanExtra(PomodoroTimerServiceExtras.INTENT_TIMER_IS_FOCUS, true)
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0) // maxTime은 초 단위로 전달
        val action = intent.action

        Timber.d("[지훈] ${object {}.javaClass.enclosingMethod?.name} isFocus $isFocus / $action")

        when (action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                if (isFocus) {
                    startFocusTimer(maxTime)
                } else {
                    startRestTimer(maxTime)
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

    private fun startFocusTimer(maxTime: Int) {
        if (focusTimer == null) {
            focusTimeElapsed = 0
            focusTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    focusTimeElapsed += (TIMER_DELAY / TIMER_DELAY).toInt() // 경과 시간 누적 (TIMER_DELAY는 밀리초이므로 초 단위로 변환)

                    if (focusTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.d("[지훈] Focus 타이머가 maxTime $maxTime 에 도달하여 중지됨")
                        stopFocusTimer()
                    } else {
                        Timber.d("[지훈] Focus 타이머 작동 중 / 경과 시간: $focusTimeElapsed")
                        pomodoroTimerRepository.incrementFocusedTime()
                    }
                }
            }
        }
    }

    private fun stopFocusTimer() {
        focusTimer?.cancel()
        focusTimer = null
        Timber.d("[지훈] Focus 타이머 중지")
    }

    private fun startRestTimer(maxTime: Int) {
        if (restTimer == null) {
            restTimeElapsed = 0
            restTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    restTimeElapsed += (TIMER_DELAY / TIMER_DELAY).toInt() // 경과 시간 누적 (TIMER_DELAY는 밀리초이므로 초 단위로 변환)
                    if (restTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.d("[지훈] Rest 타이머가 maxTime $maxTime 에 도달하여 중지됨")
                        stopRestTimer()
                    } else {
                        Timber.d("[지훈] Rest 타이머 작동 중 / 경과 시간: $restTimeElapsed")
                        pomodoroTimerRepository.incrementRestedTime()
                    }
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
