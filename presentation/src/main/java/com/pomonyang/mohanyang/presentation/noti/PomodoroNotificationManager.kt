package com.pomonyang.mohanyang.presentation.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.di.PomodoroNotification
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_NAME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PomodoroNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @PomodoroNotification private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManager,
    private val pomodoroNotificationContentFactory: PomodoroNotificationContentFactory
) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            POMODORO_NOTIFICATION_CHANNEL_ID,
            POMODORO_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
            enableLights(false)
            enableVibration(false)
            vibrationPattern = null
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(category: PomodoroCategoryType? = null): Notification {
        val remoteViews = pomodoroNotificationContentFactory.createPomodoroNotificationContent(
            category = category,
            time = context.getString(R.string.notification_timer_default_time),
            overtime = null
        )
        return notificationBuilder
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setColor(ContextCompat.getColor(context, R.color.notification_background_color))
            .setColorized(true)
            .build()
    }

    fun updateNotification(category: PomodoroCategoryType?, time: String, overtime: String) {
        val remoteViews = pomodoroNotificationContentFactory.createPomodoroNotificationContent(
            category = category,
            time = time,
            overtime = overtime
        )
        val notification = notificationBuilder
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setColor(ContextCompat.getColor(context, R.color.notification_background_color))
            .setColorized(true)
            .build()

        notificationManager.notify(POMODORO_NOTIFICATION_ID, notification)
    }
}
