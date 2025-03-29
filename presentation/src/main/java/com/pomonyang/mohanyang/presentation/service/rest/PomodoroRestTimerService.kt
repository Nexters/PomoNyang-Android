package com.pomonyang.mohanyang.presentation.service.rest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.di.RestTimerType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.noti.PomodoroNotificationManager
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryModel
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerEventHandler
import com.pomonyang.mohanyang.presentation.service.PomodoroTimerServiceExtras
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
internal class PomodoroRestTimerService :
    Service(),
    PomodoroTimerEventHandler {

    @RestTimerType
    @Inject
    lateinit var restTimer: PomodoroTimer

    @Inject
    lateinit var pomodoroNotificationManager: PomodoroNotificationManager

    @Inject
    lateinit var timerRepository: PomodoroTimerRepository

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timerId = intent.getStringExtra(PomodoroTimerServiceExtras.INTENT_TIMER_ID) ?: run {
            throw Exception("timerId is null or blank")
        }
        val maxTime = intent.getIntExtra(PomodoroTimerServiceExtras.INTENT_TIMER_MAX_TIME, 0)
        when (intent.action) {
            PomodoroTimerServiceExtras.ACTION_TIMER_START -> {
                startForeground(
                    PomodoroConstants.POMODORO_NOTIFICATION_ID,
                    pomodoroNotificationManager.createNotification(),
                )
                restTimer.startTimer(
                    timerId = timerId,
                    maxTime = maxTime,
                    eventHandler = this,
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
        Timber.tag("TIMER").d("onRestTimeEnd")
        MnNotificationManager.notifyRestEnd(context = this)
    }

    override fun onTimeExceeded() {
        // TODO 여기 뭔가 로직이 필요하면 그때 추가
    }

    override fun updateTimer(
        timerId: String,
        time: String,
        overtime: String,
        category: CategoryModel?,
    ) {
        scope.launch {
            timerRepository.incrementRestedTime(timerId)
        }
        pomodoroNotificationManager.updateNotification(
            time = time,
            overtime = overtime,
            category = category,
        )
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(STOP_FOREGROUND_REMOVE)
        restTimer.stopTimer()
        return super.stopService(name)
    }
}
