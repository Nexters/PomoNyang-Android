package com.pomonyang.mohanyang.notification.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.pomonyang.mohanyang.R
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
    .setSmallIcon(com.mohanyang.presentation.R.drawable.ic_null)
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

fun Context.isNotificationGranted(): Boolean = (
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
    )
