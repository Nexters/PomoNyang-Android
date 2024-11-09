package com.pomonyang.mohanyang.presentation.service.rest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.presentation.di.RestTimerType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
internal class PomodoroRestTimerService :
    Service(),
    PomodoroTimerEventHandler {

    @RestTimerType
    @Inject
    lateinit var restTimer: PomodoroTimer

    @Inject
    lateinit var pomodoroNotificationManager: PomodoroNotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0)
        when (intent.action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                startForeground(
                    Random.nextInt(),
                    pomodoroNotificationManager.createNotification(false)
                )
                restTimer.startTimer(
                    maxTime = maxTime,
                    eventHandler = this,
                    pomodoroNotificationManager = pomodoroNotificationManager
                )
            }

            PomodoroTimerServiceExtras.ACTION_TIMER_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                restTimer.stopTimer()
            }
        }

        return START_NOT_STICKY
    }

    override fun onTimeEnd() {
        pomodoroNotificationManager.notifyRestEnd()
    }

    override fun onTimeExceeded() {
        pomodoroNotificationManager.notifyRestExceed()
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(STOP_FOREGROUND_REMOVE)
        restTimer.stopTimer()
        return super.stopService(name)
    }
}
