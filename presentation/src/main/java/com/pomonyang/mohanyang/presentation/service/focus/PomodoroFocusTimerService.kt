package com.pomonyang.mohanyang.presentation.service.focus

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.di.FocusTimerType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import com.pomonyang.mohanyang.presentation.util.getSerializableExtraCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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

    @Inject
    lateinit var timerRepository: PomodoroTimerRepository

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0)
        val category = intent.getSerializableExtraCompat<PomodoroCategoryType>(PomodoroTimerServiceExtras.INTENT_FOCUS_CATEGORY)

        Timber.tag("TIMER").d("onStartCommand > ${intent.action} / maxTime: $maxTime / category $category")
        when (intent.action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                startForeground(
                    POMODORO_NOTIFICATION_ID,
                    pomodoroNotificationManager.createNotification(category),
                )
                focusTimer.startTimer(
                    maxTime = maxTime,
                    eventHandler = this,
                    category = category,
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
        Timber.tag("TIMER").d("onFocusTimeEnd")
        MnNotificationManager.notifyFocusEnd(context = this)
    }

    override fun onTimeExceeded() {
        // TODO 여기 뭔가 로직이 필요하면 그때 추가
    }

    override fun updateTimer(time: String, overtime: String, category: PomodoroCategoryType?) {
        scope.launch {
            timerRepository.incrementFocusedTime()
        }
        pomodoroNotificationManager.updateNotification(
            time = time,
            overtime = overtime,
            category = category,
        )
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(STOP_FOREGROUND_REMOVE)
        focusTimer.stopTimer()
        return super.stopService(name)
    }
}
