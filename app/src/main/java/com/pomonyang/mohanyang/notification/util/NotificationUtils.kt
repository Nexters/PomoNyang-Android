package com.pomonyang.mohanyang.notification.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            MnNotificationManager.setCustomAlarmSound(applicationContext, this)
        }
    notificationManager.createNotificationChannel(channel)
}

fun Context.defaultNotification(
    pendingIntent: PendingIntent? = null,
    channelId: String,
): NotificationCompat.Builder = NotificationCompat.Builder(
    this,
    channelId,
)
    .setContentIntent(pendingIntent)
    .setSmallIcon(R.drawable.ic_app_notification)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setColor(ContextCompat.getColor(this, com.mohanyang.presentation.R.color.notification_background))
    .setColorized(true)
    .setAutoCancel(true)
    .setOnlyAlertOnce(true)
    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
    .setGroup(getString(R.string.channel_group_name))

fun Context.summaryNotification(
    pendingIntent: PendingIntent? = null,
    channelId: String,
): NotificationCompat.Builder = this.defaultNotification(pendingIntent, channelId)
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

fun Context.deleteNotificationChannelIfExists(channelId: String) {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val existingChannel = notificationManager.getNotificationChannel(channelId)
    if (existingChannel != null) {
        notificationManager.deleteNotificationChannel(channelId)
    }
}

fun Context.createInterruptNotificationChannel() {
    val notificationManager =
        applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val channelId = applicationContext.getString(R.string.interrupt_channel_id)
    val channelName = applicationContext.getString(R.string.interrupt_channel_name)

    val channel =
        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            setSound(null, null)
        }
    notificationManager.createNotificationChannel(channel)
}
