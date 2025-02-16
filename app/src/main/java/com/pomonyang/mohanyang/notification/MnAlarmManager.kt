package com.pomonyang.mohanyang.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.notification.util.getTriggerTimeInMillis
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

class MnAlarmManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val alarmManager: AlarmManager,
) {
    @SuppressLint("ScheduleExactAlarm")
    fun createAlarm(
        scheduleTime: LocalTime,
        channelId: String,
        id: Int = UUID.randomUUID().hashCode(),
        title: String = "",
        message: String = "",
    ): PendingIntent = with(applicationContext) {
        val intent =
            Intent(this, LocalNotificationReceiver::class.java).apply {
                putExtra(getString(R.string.local_notification_id), id)
                putExtra(getString(R.string.local_notification_title), title)
                putExtra(getString(R.string.local_notification_message), message)
                putExtra(getString(R.string.local_notification_channel_id), channelId)
                action = MnNotificationManager.INTENT_SEND_MESSAGE
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val triggerTime = getTriggerTimeInMillis(scheduleTime)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent,
        )
        return pendingIntent
    }

    fun cancelAlarm(pendingIntent: PendingIntent) {
        alarmManager.cancel(pendingIntent)
    }
}
