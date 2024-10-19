package com.pomonyang.mohanyang.presentation.screen.pomodoro.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.pomonyang.mohanyang.presentation.di.PomodoroNotification
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_NAME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import javax.inject.Inject

internal class PomodoroNotificationManager @Inject constructor(
    @PomodoroNotification private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManager
) {
    fun createNotification(isFocus: Boolean): Notification {
        val notificationChannelId = POMODORO_NOTIFICATION_CHANNEL_ID
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            POMODORO_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)

        return notificationBuilder
            .setContentText(if (isFocus) "집중 시간이다냥" else "휴식 시간이다냥")
            .build().apply {
                flags = Notification.FLAG_NO_CLEAR
            }
    }

    fun notifyFocusEnd() {
        notificationManager.notify(
            POMODORO_NOTIFICATION_ID,
            notificationBuilder.setContentText("집중 시간이 끝났습니다!").build()
        )
    }

    fun notifyFocusExceed() {
        notificationManager.notify(
            POMODORO_NOTIFICATION_ID,
            notificationBuilder.setContentText(
                "너무 오랫동안 자리를 비웠다냥"
            ).build()
        )
    }

    fun notifyRestEnd() {
        notificationManager.notify(
            POMODORO_NOTIFICATION_ID,
            notificationBuilder.setContentText("휴식 시간이 끝났습니다!").build()
        )
    }

    fun notifyRestExceed() {
        notificationManager.notify(
            POMODORO_NOTIFICATION_ID,
            notificationBuilder.setContentText(
                "너무 오랫동안 자리를 비웠다냥"
            ).build()
        )
    }
}
