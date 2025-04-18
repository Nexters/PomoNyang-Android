package com.pomonyang.mohanyang.presentation.noti

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.RemoteViews
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryModel
import com.pomonyang.mohanyang.presentation.util.formatTime
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import timber.log.Timber

internal class PomodoroNotificationContentFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bitmapGenerator: PomodoroNotificationBitmapGenerator,
) {

    fun createPomodoroNotificationContent(isRest: Boolean): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro_standard)

        val titleBitmap = generateTitleBitmap(isRest)
        val contentBitmap = generateContentBitmap(isRest)

        setTitle(remoteViews, titleBitmap)
        setContent(remoteViews, contentBitmap)
        setRightContent(remoteViews)

        return remoteViews
    }

    fun createPomodoroNotificationBigContent(
        category: CategoryModel?,
        time: String,
        overtime: String?,
    ): RemoteViews {
        Timber.tag("koni").d("createPomodoroNotificationBigContent > $category")
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro_expand)

        val formattedTime = formatTimeString(time)
        val formattedOvertime = formatOvertimeString(overtime)

        val (statusBitmap, iconRes) = getStatusBitmapAndIcon(category)

        setStatus(remoteViews, statusBitmap)
        setTime(remoteViews, formattedTime)
        setOvertime(remoteViews, formattedOvertime)
        setIcon(remoteViews, iconRes)
        setRightContent(remoteViews)

        return remoteViews
    }

    private fun generateTitleBitmap(isRest: Boolean): Bitmap {
        val titleRes = if (isRest) R.string.notification_rest_title else R.string.notification_focus_title
        return bitmapGenerator.createTextBitmap(
            text = titleRes,
            color = R.color.notification_pomodoro_title,
            font = R.font.pretendard_semibold,
            textSize = 14f,
        )
    }

    private fun generateContentBitmap(isRest: Boolean): Bitmap {
        val contentRes = if (isRest) R.string.notification_rest_content else R.string.notification_focus_content
        return bitmapGenerator.createTextBitmap(
            text = contentRes,
            color = R.color.notification_pomodoro_content,
            font = R.font.pretendard_regular,
            textSize = 14f,
        )
    }

    private fun setTitle(remoteViews: RemoteViews, titleBitmap: Bitmap) {
        remoteViews.setImageViewBitmap(R.id.text_title, titleBitmap)
    }

    private fun setContent(remoteViews: RemoteViews, contentBitmap: Bitmap) {
        remoteViews.setImageViewBitmap(R.id.text_content, contentBitmap)
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
        val timeBitmap = bitmapGenerator.combineTimeBitmaps(
            time = formattedTime,
            isOvertime = false,
        )
        remoteViews.setImageViewBitmap(R.id.text_time, timeBitmap)
    }

    private fun setOvertime(remoteViews: RemoteViews, formattedOvertime: String?) {
        if (formattedOvertime != null) {
            val overtimeBitmap = bitmapGenerator.combineTimeBitmaps(
                time = formattedOvertime,
                isOvertime = true,
            )
            remoteViews.setImageViewBitmap(R.id.text_overtime, overtimeBitmap)
            remoteViews.setViewVisibility(R.id.text_overtime, View.VISIBLE)
        } else {
            remoteViews.setViewVisibility(R.id.text_overtime, View.GONE)
        }
    }

    private fun setIcon(remoteViews: RemoteViews, iconRes: Int) {
        remoteViews.setImageViewResource(R.id.iv_category, iconRes)
    }

    private fun setRightContent(remoteViews: RemoteViews) {
        remoteViews.setImageViewResource(R.id.iv_right_content, R.drawable.img_touch_hair_ball)
    }

    private fun getStatusBitmapAndIcon(category: CategoryModel?): Pair<Bitmap, Int> {
        val statusText = category?.name ?: context.getString(R.string.notification_timer_rest)
        val statusBitmap = bitmapGenerator.createStatusBitmap(statusText)
        Timber.tag("koni").d("category?.icon?.resourceId ${category?.icon?.resourceId}")
        val iconRes = category?.icon?.resourceId ?: R.drawable.ic_lightning
        return statusBitmap to iconRes
    }
}
