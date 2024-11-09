package com.pomonyang.mohanyang.presentation.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.RemoteViews
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
    private val contentFactory: PomodoroNotificationContentFactory
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
        val isRest = category == null
        val defaultTime = context.getString(R.string.notification_timer_default_time)
        val contentView = createContentView(isRest)
        val bigContentView = createBigContentView(category, defaultTime, null)
        return buildNotification(contentView, bigContentView)
    }

    fun updateNotification(category: PomodoroCategoryType?, time: String, overtime: String) {
        val isRest = category == null
        val contentView = createContentView(isRest)
        val bigContentView = createBigContentView(category, time, overtime)
        val notification = buildNotification(contentView, bigContentView)
        notificationManager.notify(POMODORO_NOTIFICATION_ID, notification)
    }

    private fun createContentView(isRest: Boolean): RemoteViews = contentFactory.createPomodoroNotificationContent(isRest)

    private fun createBigContentView(
        category: PomodoroCategoryType?,
        time: String,
        overtime: String?
    ): RemoteViews = contentFactory.createPomodoroNotificationBigContent(
        category = category,
        time = time,
        overtime = overtime
    )

    private fun buildNotification(
        contentView: RemoteViews,
        bigContentView: RemoteViews
    ): Notification = notificationBuilder
        .setCustomContentView(contentView)
        .setCustomBigContentView(bigContentView)
        .setColor(ContextCompat.getColor(context, R.color.notification_background_color))
        .setColorized(true)
        .build()
}
