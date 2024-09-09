package com.pomonyang.mohanyang.presentation.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.di.PomodoroNotification
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_NAME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.notifyFocusEnd
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager.notifyRestEnd
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
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

    @PomodoroNotification
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

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
                startForeground(POMODORO_NOTIFICATION_ID, createNotification(isFocus))
                if (isFocus) {
                    startFocusTimer(maxTime)
                } else {
                    startRestTimer(maxTime)
                }
            }

            PomodoroTimerServiceExtras.ACTION_TIMER_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                if (isFocus) {
                    stopFocusTimer()
                } else {
                    stopRestTimer()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        Timber.tag(TAG).d("stopService")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopFocusTimer()
        stopRestTimer()
        return super.stopService(name)
    }

    private fun startFocusTimer(maxTime: Int) {
        if (focusTimer == null) {
            var focusTimeElapsed = 0
            Timber.tag(TAG).d("Focus 타이머 시작 ${this@PomodoroTimerService.hashCode()}")
            focusTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    focusTimeElapsed += ONE_SECOND

                    if (focusTimeElapsed == maxTime) {
                        Timber.tag(TAG).d("Focus 타이머가 maxTime $maxTime 에 도달하여 집중 끝 알림 발송")
                        notifyFocusEnd(this@PomodoroTimerService)
                    }

                    if (focusTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.tag(TAG).d("Focus 타이머가 최대 머무를 수 있는 ${maxTime + MAX_EXCEEDED_TIME} 에 도달하여 중지됨")
                        notificationManager.notify(
                            POMODORO_NOTIFICATION_ID,
                            notificationBuilder.setContentText(
                                "너무 오랫동안 자리를 비웠다냥"
                            ).build()
                        )
                        stopFocusTimer()
                    } else {
                        Timber.tag(TAG).d("Focus 타이머 작동 중 / 경과 시간: $focusTimeElapsed ${this@PomodoroTimerService.hashCode()}")
                        pomodoroTimerRepository.incrementFocusedTime()
                    }
                }
            }
        }
    }

    private fun startRestTimer(maxTime: Int) {
        if (restTimer == null) {
            var restTimeElapsed = 0
            Timber.tag(TAG).d("Rest 타이머 시작 ${this@PomodoroTimerService.hashCode()}")
            restTimer = fixedRateTimer(initialDelay = TIMER_DELAY, period = TIMER_DELAY) {
                scope.launch {
                    restTimeElapsed += ONE_SECOND

                    if (restTimeElapsed == maxTime) {
                        Timber.tag(TAG).d("Rest 타이머가 maxTime $maxTime 에 도달하여 휴식 끝 알림 발송")
                        notifyRestEnd(this@PomodoroTimerService)
                    }

                    if (restTimeElapsed > maxTime + MAX_EXCEEDED_TIME) {
                        Timber.tag(TAG).d("Rest 타이머가 최대 머무를 수 있는 ${maxTime + MAX_EXCEEDED_TIME} 에 도달하여 중지됨")
                        stopRestTimer()
                    } else {
                        Timber.tag(TAG).d("Rest 타이머 작동 중 / 경과 시간: $restTimeElapsed ${this@PomodoroTimerService.hashCode()}")
                        pomodoroTimerRepository.incrementRestedTime()
                    }
                }
            }
        }
    }

    private fun stopFocusTimer() {
        focusTimer?.cancel()
        focusTimer = null
        Timber.tag(TAG).d("Focus 타이머 중지 ${this@PomodoroTimerService.hashCode()}")
    }

    private fun stopRestTimer() {
        restTimer?.cancel()
        restTimer = null
        Timber.tag(TAG).d("Rest 타이머 중지 ${this@PomodoroTimerService.hashCode()}")
    }

    private fun createNotification(isFocus: Boolean): Notification {
        val notificationChannelId = POMODORO_NOTIFICATION_CHANNEL_ID
        val notificationChannel = NotificationChannel(
            /* id = */
            notificationChannelId,
            /* name = */
            POMODORO_NOTIFICATION_CHANNEL_NAME,
            /* importance = */
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        return notificationBuilder
            .setContentText(if (isFocus) "집중 시간이다냥" else "휴식 시간이다냥")
            .build().apply {
                flags = Notification.FLAG_NO_CLEAR
            }
    }

    companion object {
        private const val TAG = "PomodoroTimerService"
    }
}
