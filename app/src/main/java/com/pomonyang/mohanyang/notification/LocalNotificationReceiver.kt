package com.pomonyang.mohanyang.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pomonyang.mohanyang.MainActivity
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.notification.util.defaultNotification
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
import com.pomonyang.mohanyang.notification.util.summaryNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocalNotificationReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        if (!context.isNotificationGranted()) return

        val notificationId = intent.getIntExtra(context.getString(R.string.local_notification_id), 0)
        val title = intent.getStringExtra(context.getString(R.string.local_notification_title)) ?: ""
        val message = intent.getStringExtra(context.getString(R.string.local_notification_message)) ?: ""

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            context.defaultNotification(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .build()

        val summaryNotification =
            context.summaryNotification(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.apply {
            notify(notificationId, notification)
            notify(SUMMARY_ID, summaryNotification)
        }
    }

    companion object {
        private const val SUMMARY_ID = 0
    }
}
