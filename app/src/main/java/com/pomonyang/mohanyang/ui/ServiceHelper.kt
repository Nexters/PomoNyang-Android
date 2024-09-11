package com.pomonyang.mohanyang.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object ServiceHelper {

    private const val FLAG = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(
        context: Context,
        requestCode: Int
    ): PendingIntent {
        val notificationIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            notificationIntent,
            FLAG
        )
    }
}
