package com.pomonyang.mohanyang.notification.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun Context.createNotificationChannel() {
    val notificationManager =
        applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val channelId = applicationContext.getString(R.string.channel_id)
    val channelName = applicationContext.getString(R.string.channel_name)

    val channel =
        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
    notificationManager.createNotificationChannel(channel)
}

fun Context.defaultNotification(
    pendingIntent: PendingIntent? = null
): NotificationCompat.Builder = NotificationCompat.Builder(
    this,
    getString(R.string.channel_id)
)
    .setContentIntent(pendingIntent)
    .setSmallIcon(R.drawable.ic_app_notification)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setAutoCancel(true)
    .setOnlyAlertOnce(true)
    .setFullScreenIntent(pendingIntent, true)
    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
    .setGroup(getString(R.string.channel_group_name))

fun Context.summaryNotification(pendingIntent: PendingIntent? = null): NotificationCompat.Builder = this.defaultNotification(pendingIntent)
    .setGroupSummary(true)

fun getTriggerTimeInMillis(time: LocalTime): Long {
    val now = LocalDate.now()
    val dateTime = time.atDate(now)
    return dateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun Context.isNotificationGranted(): Boolean = MnNotificationManager.isNotificationGranted(this)
