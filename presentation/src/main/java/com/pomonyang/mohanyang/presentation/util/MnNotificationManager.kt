package com.pomonyang.mohanyang.presentation.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object MnNotificationManager {

    const val INTENT_NOTIFY_FOCUS_MESSAGE = "mohanyang.intent.NOTIFY_FOCUS_MESSAGE"
    const val INTENT_NOTIFY_REST_MESSAGE = "mohanyang.intent.NOTIFY_REST_MESSAGE"
    const val INTENT_START_INTERRUPT_MESSAGE = "mohanyang.intent.START_INTERRUPT_MESSAGE"
    const val INTENT_STOP_INTERRUPT_MESSAGE = "mohanyang.intent.STOP_INTERRUPT_MESSAGE"
    const val INTENT_SEND_MESSAGE = "mohanyang.intent.SEND_MESSAGE"

    val intents = listOf(
        INTENT_NOTIFY_FOCUS_MESSAGE,
        INTENT_NOTIFY_REST_MESSAGE,
        INTENT_START_INTERRUPT_MESSAGE,
        INTENT_STOP_INTERRUPT_MESSAGE,
        INTENT_SEND_MESSAGE
    )

    fun notifyFocusEnd(context: Context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(INTENT_NOTIFY_FOCUS_MESSAGE))
    }

    fun notifyRestEnd(context: Context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(INTENT_NOTIFY_REST_MESSAGE))
    }

    fun startInterrupt(context: Context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(INTENT_START_INTERRUPT_MESSAGE))
    }

    fun stopInterrupt(context: Context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(INTENT_STOP_INTERRUPT_MESSAGE))
    }

    fun isNotificationGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.areNotificationsEnabled()
        }
    }
}
