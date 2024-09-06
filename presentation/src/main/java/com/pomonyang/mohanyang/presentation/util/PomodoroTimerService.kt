package com.pomonyang.mohanyang.presentation.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.notifyFocusEnd
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.notifyRestEnd
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
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0)
        val action = intent.action

        Timber.tag(TAG).d("isFocus $isFocus / maxTime $maxTime / $action")

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
        Timber.tag(TAG).d("stopService")
        stopFocusTimer()
        stopRestTimer()
        return super.stopService(name)
    }

    private fun startFocusTimer(maxTime: Int) {
        if (focusTimer == null) {
            var focusTimeElapsed = 0
            focusTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    focusTimeElapsed += ONE_SECOND

                    if (focusTimeElapsed == maxTime) {
                        Timber.d("Focus 타이머가 maxTime $maxTime 에 도달하여 집중 끝 알림 발송")
                        notifyFocusEnd(this@PomodoroTimerService)
                    }

                    if (focusTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.d("Focus 타이머가 최대 머무를 수 있는 ${maxTime + MAX_EXCEEDED_TIME} 에 도달하여 중지됨")
                        stopFocusTimer()
                    } else {
                        Timber.d("Focus 타이머 작동 중 / 경과 시간: $focusTimeElapsed")
                        pomodoroTimerRepository.incrementFocusedTime()
                    }
                }
            }
        }
    }

    private fun stopFocusTimer() {
        focusTimer?.cancel()
        focusTimer = null
        Timber.d("Focus 타이머 중지")
    }

    private fun startRestTimer(maxTime: Int) {
        if (restTimer == null) {
            var restTimeElapsed = 0
            restTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    restTimeElapsed += ONE_SECOND

                    if (restTimeElapsed == maxTime) {
                        Timber.d("Rest 타이머가 maxTime $maxTime 에 도달하여 휴식 끝 알림 발송")
                        notifyRestEnd(this@PomodoroTimerService)
                    }

                    if (restTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.d("Rest 타이머가 최대 머무를 수 있는 ${maxTime + MAX_EXCEEDED_TIME} 에 도달하여 중지됨")
                        stopRestTimer()
                    } else {
                        Timber.d("Rest 타이머 작동 중 / 경과 시간: $restTimeElapsed")
                        pomodoroTimerRepository.incrementRestedTime()
                    }
                }
            }
        }
    }

    private fun stopRestTimer() {
        restTimer?.cancel()
        restTimer = null
        Timber.d("Rest 타이머 중지")
    }

    companion object {
        private const val TAG = "PomodoroTimerService"
    }
}
