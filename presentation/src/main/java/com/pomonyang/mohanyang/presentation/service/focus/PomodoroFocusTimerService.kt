package com.pomonyang.mohanyang.presentation.service.focus

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.pomonyang.mohanyang.presentation.di.FocusTimerType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
internal class PomodoroFocusTimerService :
    Service(),
    PomodoroTimerEventHandler {

    @FocusTimerType
    @Inject
    lateinit var focusTimer: PomodoroTimer

    @Inject
    lateinit var pomodoroNotificationManager: PomodoroNotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0)
        val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(PomodoroTimerServiceExtras.INTENT_FOCUS_CATEGORY, PomodoroCategoryType::class.java)
        } else {
            intent.getSerializableExtra(PomodoroTimerServiceExtras.INTENT_FOCUS_CATEGORY) as PomodoroCategoryType
        }

        Timber.tag("TIMER").d("onStartCommand > ${intent.action} / maxTime: $maxTime / category $category")
        when (intent.action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                startForeground(
                    POMODORO_NOTIFICATION_ID,
                    pomodoroNotificationManager.createNotification(category)
                )
                focusTimer.startTimer(
                    maxTime = maxTime,
                    eventHandler = this,
                    pomodoroNotificationManager = pomodoroNotificationManager,
                    category = category
                )
            }

            PomodoroTimerServiceExtras.ACTION_TIMER_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                focusTimer.stopTimer()
            }
        }

        return START_NOT_STICKY
    }

    override fun onTimeEnd() {
//        pomodoroNotificationManager.notifyFocusEnd()
    }

    override fun onTimeExceeded() {
//        pomodoroNotificationManager.notifyFocusExceed()
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(STOP_FOREGROUND_REMOVE)
        focusTimer.stopTimer()
        return super.stopService(name)
    }
}
