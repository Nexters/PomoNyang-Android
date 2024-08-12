package com.pomonyang.mohanyang.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.data.local.device.util.lockScreenState
import com.pomonyang.mohanyang.domain.model.cat.CatType
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FocusNotificationService : Service() {

    val selectedCatType = CatType.CHEESE

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var focusNotificationJob: Job? = null
    private val focusNotifications = ArrayList<PendingIntent>()

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private val isLocked: Flow<Boolean> = this.lockScreenState()

    @Inject
    lateinit var mnAlarmManager: MnAlarmManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        observeLockScreen()
        startFocusNotify()
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
        }.launchIn(serviceScope)
    }

    private fun stopNotify() {
        focusNotifications.run {
            forEach { mnAlarmManager.cancelAlarm(it) }
            clear()
        }
        serviceScope.cancel()
        focusNotificationJob?.cancel()
        focusNotificationJob = null
        stopSelf()
    }

    private fun startFocusNotify() {
        focusNotificationJob = serviceScope.launch {
            // 10초 후 첫 알림 전송
            delay(FIRST_DELAY)
            mnAlarmManager.createAlarm(
                LocalTime.now(),
                title = applicationContext.getString(R.string.app_name),
                message = "10초 : ${selectedCatType.pushContent}"
            ).also {
                focusNotifications.add(it)
            }
            // 30초마다 반복 알림 전송
            repeat(MAX_NOTIFICATION_COUNT) {
                delay(REPEAT_DELAY)
                mnAlarmManager.createAlarm(
                    LocalTime.now(),
                    title = applicationContext.getString(R.string.app_name),
                    message = "${(REPEAT_DELAY / 1000).toInt()}초 :  ${selectedCatType.pushContent}"
                ).also { alarm ->
                    focusNotifications.add(alarm)
                }
            }
            stopSelf()
        }
    }

    companion object {
        private const val MAX_NOTIFICATION_COUNT = 10

        // TODO 최대 수가 없다고 픽스나면 while true로 변경하기
        private const val FIRST_DELAY = 10_000L
        private const val REPEAT_DELAY = 30_000L
    }
}
