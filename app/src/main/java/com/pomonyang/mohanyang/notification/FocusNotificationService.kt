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
import com.pomonyang.mohanyang.domain.model.cat.CatType
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
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
        val isPushEnabled = applicationContext.isNotificationGranted() && runBlocking { userRepository.getMyInfo().isPushEnabled }

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
            time,
            title = applicationContext.getString(R.string.app_name),
            message = message
        ).also {
            focusNotifications.add(it)
        }
    }

    private fun startFocusNotify() {
        val selectedCatType = CatType.safeValueOf(runBlocking { userRepository.getMyInfo().cat.type })

        focusNotificationJob = scope.launch {
            // 10초 후 첫 알림 전송
            delay(FIRST_DELAY)
            addAlarm(LocalTime.now(), "10초: ${getString(selectedCatType.backgroundPushContent)}")

            // 30초마다 반복 알림 전송
            repeat(MAX_NOTIFICATION_COUNT) {
                delay(REPEAT_DELAY)
                addAlarm(LocalTime.now(), "30초: ${getString(selectedCatType.backgroundPushContent)} ${(it + 1)}/${MAX_NOTIFICATION_COUNT}")
            }
            delay(LAST_ALARM_DISPLAY_DURATION)
            stopSelf()
        }
    }

    companion object {
        // TODO 최대 수가 없다고 픽스나면 while true로 변경하기
        private const val MAX_NOTIFICATION_COUNT = 10
        private val FIRST_DELAY = if (BuildConfig.DEBUG) 2_000L else 10_000L
        private val REPEAT_DELAY = if (BuildConfig.DEBUG) 5_000L else 30_000L
        private val LAST_ALARM_DISPLAY_DURATION = REPEAT_DELAY * 3 // TODO 임시로 임의 지정
    }
}
