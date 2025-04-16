package com.pomonyang.mohanyang.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.BuildConfig
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.data.local.device.util.lockScreenState
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class FocusNotificationService : Service() {

    @Inject
    lateinit var mnAlarmManager: MnAlarmManager

    @Inject
    lateinit var userRepository: UserRepository

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var focusNotificationJob: Job? = null
    private val focusNotifications = mutableListOf<PendingIntent>()

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private val isLocked: Flow<Boolean> = this.lockScreenState()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isPushEnabled = applicationContext.isNotificationGranted()
        if (isPushEnabled) {
            observeLockScreen()
            startFocusNotify()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopNotify()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopNotify()
    }

    private fun observeLockScreen() {
        isLocked.onEach { isLocked ->
            if (isLocked) {
                stopNotify()
            }
        }.launchIn(scope)
    }

    private fun stopNotify() {
        focusNotifications.run {
            forEach { mnAlarmManager.cancelAlarm(it) }
            clear()
        }
        scope.cancel()
        focusNotificationJob?.cancel()
        focusNotificationJob = null
        stopSelf()
    }

    private fun addAlarm(time: LocalTime, message: String = "") {
        mnAlarmManager.createAlarm(
            scheduleTime = time,
            channelId = getString(R.string.interrupt_channel_id),
            title = applicationContext.getString(R.string.app_name),
            message = message,
        ).also {
            focusNotifications.add(it)
        }
    }

    private fun startFocusNotify() {
        val selectedCatType = CatType.safeValueOf(runBlocking { userRepository.getMyInfo().cat.type })

        focusNotificationJob = scope.launch {
            // 10초 후 첫 알림 전송
            delay(FIRST_DELAY)
            addAlarm(LocalTime.now(), getString(selectedCatType.backgroundPushContent))

            // 30초마다 반복 알림 전송
            /* 무한 발송 */
            while (true) {
                delay(REPEAT_DELAY)
                addAlarm(LocalTime.now(), getString(selectedCatType.backgroundPushContent))
            }
            // delay(LAST_ALARM_DISPLAY_DURATION)
            // stopSelf()
        }
    }

    companion object {
        private const val MAX_NOTIFICATION_COUNT = 10
        private val FIRST_DELAY = if (BuildConfig.DEBUG) 2_000L else 10_000L
        private val REPEAT_DELAY = if (BuildConfig.DEBUG) 5_000L else 30_000L
        private val LAST_ALARM_DISPLAY_DURATION = REPEAT_DELAY * 3 // TODO 임시로 임의 지정
    }
}
