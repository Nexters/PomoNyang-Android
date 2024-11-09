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

    fun createPomodoroNotificationContent(isRest: Boolean): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro_standard)

        val titleBitmap = generateTitleBitmap(isRest)
        val contentBitmap = generateContentBitmap(isRest)

        setTitle(remoteViews, titleBitmap)
        setContent(remoteViews, contentBitmap)

        return remoteViews
    }

    fun createPomodoroNotificationBigContent(
        category: PomodoroCategoryType?,
        time: String,
        overtime: String?
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro_expand)

        val formattedTime = formatTimeString(time)
        val formattedOvertime = formatOvertimeString(overtime)

        val (statusBitmap, iconRes) = getStatusBitmapAndIcon(category)

        setStatus(remoteViews, statusBitmap)
        setTime(remoteViews, formattedTime)
        setOvertime(remoteViews, formattedOvertime)
        setIcon(remoteViews, iconRes)

        return remoteViews
    }

    private fun generateTitleBitmap(isRest: Boolean): Bitmap {
        val titleRes = if (isRest) R.string.notification_rest_title else R.string.notification_focus_title
        return bitmapGenerator.createTextBitmap(
            text = titleRes,
            color = R.color.notification_pomodoro_title,
            font = R.font.pretendard_semibold,
            textSize = 14f
        )
    }

    private fun generateContentBitmap(isRest: Boolean): Bitmap {
        val contentRes = if (isRest) R.string.notification_rest_content else R.string.notification_focus_content
        return bitmapGenerator.createTextBitmap(
            text = contentRes,
            color = R.color.notification_pomodoro_content,
            font = R.font.pretendard_regular,
            textSize = 14f
        )
    }

    private fun setTitle(remoteViews: RemoteViews, titleBitmap: Bitmap) {
        remoteViews.setImageViewBitmap(R.id.iv_text_title, titleBitmap)
    }

    private fun setContent(remoteViews: RemoteViews, contentBitmap: Bitmap) {
        remoteViews.setImageViewBitmap(R.id.iv_text_content, contentBitmap)
    }

    private fun formatTimeString(timeStr: String): String {
        val totalSeconds = timeStr.toIntOrNull()
        return totalSeconds?.formatTime() ?: context.getString(R.string.notification_timer_default_time)
    }

    private fun formatOvertimeString(overtime: String?): String? = if (!overtime.isNullOrEmpty() && overtime != "0") {
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

    private fun getStatusBitmapAndIcon(category: PomodoroCategoryType?): Pair<Bitmap, Int> {
        val statusTextRes = category?.kor ?: R.string.notification_timer_rest
        val statusText = context.getString(statusTextRes)
        val statusBitmap = bitmapGenerator.createStatusBitmap(statusText)
        val iconRes = category?.iconRes ?: R.drawable.ic_rest
        return statusBitmap to iconRes
    }
}
