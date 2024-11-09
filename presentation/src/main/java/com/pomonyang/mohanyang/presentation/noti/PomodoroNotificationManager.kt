package com.pomonyang.mohanyang.presentation.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.di.PomodoroNotification
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_NAME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PomodoroNotificationManager @Inject constructor(
    @PomodoroNotification private val notificationBuilder: NotificationCompat.Builder,
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {
    private val timeBitmaps by lazy {
        val typeface = ResourcesCompat.getFont(context, R.font.pretendard_bold)!!
        val textColor = ContextCompat.getColor(context, R.color.notification_pomodoro_time)

        (0..9).associate { number ->
            number.toString() to textToBitmap(
                number.toString(),
                typeface,
                40f,
                textColor
            )
        }
    }

    private val overtimeNumberBitmaps by lazy {
        val typeface = ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!
        val textColor = ContextCompat.getColor(context, R.color.notification_pomodoro_over_time)

        (0..9).associate { number ->
            number.toString() to textToBitmap(
                number.toString(),
                typeface,
                18f,
                textColor
            )
        }
    }

    private val colonBitmap by lazy {
        textToBitmap(
            ":",
            ResourcesCompat.getFont(context, R.font.pretendard_bold)!!,
            40f,
            ContextCompat.getColor(context, R.color.notification_pomodoro_time)
        )
    }

    private val overtimeColonBitmap by lazy {
        textToBitmap(
            ":",
            ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!,
            18f,
            ContextCompat.getColor(context, R.color.notification_pomodoro_over_time)
        )
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            POMODORO_NOTIFICATION_CHANNEL_ID,
            POMODORO_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
            enableLights(false)
            enableVibration(false)
            vibrationPattern = null
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(
        category: PomodoroCategoryType? = null
    ): Notification {
        val (statusBitmap, iconRes) = if (category != null) {
            val statusText = context.getString(category.kor)
            val bitmap = createStatusBitmap(statusText)
            bitmap to category.iconRes
        } else {
            val statusText = "휴식중"
            val bitmap = createStatusBitmap(statusText)
            bitmap to R.drawable.ic_rest
        }

        val remoteViews = createRemoteViews(statusBitmap, iconRes, "00:00", null)

        return notificationBuilder
            .setAutoCancel(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setColor(ContextCompat.getColor(context, R.color.notification_background_color))
            .setColorized(true)
            .setVibrate(null)
            .build()
    }

    private fun createStatusBitmap(statusText: String): Bitmap = textToBitmap(
        statusText,
        ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!,
        16f,
        ContextCompat.getColor(context, R.color.notification_pomodoro_category_text)
    )

    private fun createRemoteViews(statusBitmap: Bitmap, iconRes: Int, time: String, overtime: String?): RemoteViews = RemoteViews(context.packageName, R.layout.notification_pomodoro).apply {
        // 상태 텍스트 설정
        setImageViewBitmap(R.id.text_status, statusBitmap)

        // 시간 설정 (항상 표시)
        val timeDigits = time.toCharArray()
        val timeBitmap = combineBitmaps(timeDigits, false)
        setImageViewBitmap(R.id.text_time, timeBitmap)

        if (overtime != null) {
            // 초과 시간 설정
            val overtimeDigits = overtime.toCharArray()
            val overtimeBitmap = combineBitmaps(overtimeDigits, true)
            setImageViewBitmap(R.id.text_overtime, overtimeBitmap)
            setViewVisibility(R.id.text_overtime, View.VISIBLE)
        } else {
            // 초과 시간 숨기기
            setViewVisibility(R.id.text_overtime, View.GONE)
        }

        // 아이콘 설정
        setImageViewResource(R.id.icon_lightning, iconRes)
    }

    private fun textToBitmap(text: String, typeface: Typeface, textSize: Float, textColor: Int): Bitmap {
        val paint = Paint().apply {
            this.typeface = typeface
            this.textSize = textSize * context.resources.displayMetrics.density
            this.color = textColor
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }

        val baseline = -paint.ascent()
        val width = (paint.measureText(text) + 0.5f).toInt()
        val height = (baseline + paint.descent() + 0.5f).toInt()

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            Canvas(this).drawText(text, 0f, baseline, paint)
        }
    }

    private fun combineBitmaps(digits: CharArray, isOvertime: Boolean): Bitmap {
        var totalWidth = 0
        var maxHeight = 0

        // 각 문자에 대한 비트맵 리스트 생성
        val bitmaps = mutableListOf<Bitmap>()

        digits.forEach { digit ->
            val bitmap = when (digit) {
                ':' -> if (isOvertime) overtimeColonBitmap else colonBitmap
                else -> if (isOvertime) overtimeNumberBitmaps[digit.toString()] else timeBitmaps[digit.toString()]
            }
            bitmap?.let {
                bitmaps.add(it)
                totalWidth += it.width
                maxHeight = maxOf(maxHeight, it.height)
            }
        }

        // overtime인 경우 " 초과" 텍스트 추가
        if (isOvertime) {
            val extraTextBitmap = textToBitmap(
                " 초과",
                ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!,
                18f,
                ContextCompat.getColor(context, R.color.notification_pomodoro_over_time)
            )
            bitmaps.add(extraTextBitmap)
            totalWidth += extraTextBitmap.width
            maxHeight = maxOf(maxHeight, extraTextBitmap.height)
        }

        // 새로운 비트맵 생성 및 비트맵들 그리기
        return Bitmap.createBitmap(totalWidth, maxHeight, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            var currentX = 0f

            bitmaps.forEach { bitmap ->
                canvas.drawBitmap(bitmap, currentX, 0f, null)
                currentX += bitmap.width
            }
        }
    }

    fun updateNotification(category: PomodoroCategoryType?, time: String, overtime: String) {
        val formattedTime = formatTimeString(time)
        val formattedOvertime = if (overtime != "0") {
            formatTimeString(overtime)
        } else {
            null
        }

        val (statusBitmap, iconRes) = if (category != null) {
            val statusText = context.getString(category.kor)
            val bitmap = createStatusBitmap(statusText)
            bitmap to category.iconRes
        } else {
            val statusText = "휴식중"
            val bitmap = createStatusBitmap(statusText)
            bitmap to R.drawable.ic_rest
        }

        val remoteViews = createRemoteViews(statusBitmap, iconRes, formattedTime, formattedOvertime)

        notificationManager.notify(
            POMODORO_NOTIFICATION_ID,
            notificationBuilder
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setVibrate(null)
                .build()
        )
    }

    private fun formatTimeString(timeStr: String): String {
        val totalSeconds = timeStr.toIntOrNull()
        return if (totalSeconds != null) {
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            String.format("%02d:%02d", minutes, seconds)
        } else {
            "00:00"
        }
    }
}
