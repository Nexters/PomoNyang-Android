package com.pomonyang.mohanyang.presentation.noti

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.RemoteViews
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.util.formatTime
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PomodoroNotificationContentFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bitmapGenerator: PomodoroNotificationBitmapGenerator
) {

    fun createPomodoroNotificationContent(
        category: PomodoroCategoryType?,
        time: String,
        overtime: String?
    ): RemoteViews {
        val formattedTime = formatTimeString(time)
        val formattedOvertime = formatOvertimeString(overtime)

        val (statusBitmap, iconRes) = getStatusBitmapAndIcon(category)
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro)

        setStatus(remoteViews, statusBitmap)
        setTime(remoteViews, formattedTime)
        setOvertime(remoteViews, formattedOvertime)
        setIcon(remoteViews, iconRes)

        return remoteViews
    }

    private fun formatTimeString(timeStr: String): String {
        val totalSeconds = timeStr.toIntOrNull()
        return totalSeconds?.formatTime() ?: context.getString(R.string.notification_timer_default_time)
    }

    private fun formatOvertimeString(overtime: String?): String? = if (overtime != null && overtime != "0") {
        formatTimeString(overtime)
    } else {
        null
    }

    private fun setStatus(remoteViews: RemoteViews, statusBitmap: Bitmap) {
        remoteViews.setImageViewBitmap(R.id.text_status, statusBitmap)
    }

    private fun setTime(remoteViews: RemoteViews, formattedTime: String) {
        val timeBitmap = bitmapGenerator.combineTimeBitmaps(formattedTime, isOvertime = false)
        remoteViews.setImageViewBitmap(R.id.text_time, timeBitmap)
    }

    private fun setOvertime(remoteViews: RemoteViews, formattedOvertime: String?) {
        if (formattedOvertime != null) {
            val overtimeBitmap = bitmapGenerator.combineTimeBitmaps(formattedOvertime, isOvertime = true)
            remoteViews.setImageViewBitmap(R.id.text_overtime, overtimeBitmap)
            remoteViews.setViewVisibility(R.id.text_overtime, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.text_overtime, View.GONE)
        }
    }

    private fun setIcon(remoteViews: RemoteViews, iconRes: Int) {
        remoteViews.setImageViewResource(R.id.icon_category, iconRes)
    }

    private fun getStatusBitmapAndIcon(category: PomodoroCategoryType?): Pair<Bitmap, Int> = if (category != null) {
        val statusText = context.getString(category.kor)
        val bitmap = bitmapGenerator.createStatusBitmap(statusText)
        bitmap to category.iconRes
    } else {
        val statusText = context.getString(R.string.notification_timer_rest)
        val bitmap = bitmapGenerator.createStatusBitmap(statusText)
        bitmap to R.drawable.ic_rest
    }
}
