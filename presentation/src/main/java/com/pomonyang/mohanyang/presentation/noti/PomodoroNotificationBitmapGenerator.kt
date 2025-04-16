package com.pomonyang.mohanyang.presentation.noti

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mohanyang.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PomodoroNotificationBitmapGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val timeBitmaps: Map<String, Bitmap> by lazy {
        generateNumberBitmaps(
            fontResId = R.font.pretendard_bold,
            textSize = 40f,
            colorResId = R.color.notification_pomodoro_time,
        )
    }

    private val overtimeNumberBitmaps: Map<String, Bitmap> by lazy {
        generateNumberBitmaps(
            fontResId = R.font.pretendard_semibold,
            textSize = 18f,
            colorResId = R.color.notification_pomodoro_over_time,
        )
    }

    private val colonBitmap: Bitmap by lazy {
        createTextBitmap(
            text = ":",
            fontResId = R.font.pretendard_bold,
            textSize = 40f,
            colorResId = R.color.notification_pomodoro_time,
        )
    }

    private val overtimeColonBitmap: Bitmap by lazy {
        createTextBitmap(
            text = ":",
            fontResId = R.font.pretendard_semibold,
            textSize = 18f,
            colorResId = R.color.notification_pomodoro_over_time,
        )
    }

    fun createStatusBitmap(statusText: String): Bitmap = createTextBitmap(
        text = statusText,
        fontResId = R.font.pretendard_semibold,
        textSize = 16f,
        colorResId = R.color.notification_pomodoro_category_text,
    )

    fun combineTimeBitmaps(time: String, isOvertime: Boolean): Bitmap {
        val digits = time.toCharArray()
        return combineBitmaps(
            digits = digits,
            isOvertime = isOvertime,
        )
    }

    fun createTextBitmap(
        @StringRes text: Int,
        @ColorRes color: Int,
        @FontRes font: Int,
        textSize: Float,
    ): Bitmap = createTextBitmap(
        text = context.getString(text),
        fontResId = font,
        textSize = textSize,
        colorResId = color,
    )

    private fun generateNumberBitmaps(@FontRes fontResId: Int, textSize: Float, @ColorRes colorResId: Int): Map<String, Bitmap> = (0..9).associate { number ->
        number.toString() to createTextBitmap(
            text = number.toString(),
            fontResId = fontResId,
            textSize = textSize,
            colorResId = colorResId,
        )
    }

    private fun createTextBitmap(text: String, @FontRes fontResId: Int, textSize: Float, @ColorRes colorResId: Int): Bitmap {
        val typeface = ResourcesCompat.getFont(context, fontResId)!!
        val textColor = ContextCompat.getColor(context, colorResId)
        return textToBitmap(text, typeface, textSize, textColor)
    }

    private fun textToBitmap(
        text: String,
        typeface: Typeface,
        textSize: Float,
        textColor: Int,
    ): Bitmap {
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

    private fun combineBitmaps(
        digits: CharArray,
        isOvertime: Boolean,
    ): Bitmap {
        val bitmaps = mutableListOf<Bitmap>()
        var totalWidth = 0
        var maxHeight = 0

        digits.forEach { digit ->
            val bitmap = when (digit) {
                ':' -> if (isOvertime) overtimeColonBitmap else colonBitmap
                else -> {
                    val digitStr = digit.toString()
                    if (isOvertime) overtimeNumberBitmaps[digitStr] else timeBitmaps[digitStr]
                }
            }
            bitmap?.let {
                bitmaps.add(it)
                totalWidth += it.width
                maxHeight = maxOf(maxHeight, it.height)
            }
        }

        if (isOvertime) {
            val extraTextBitmap = createTextBitmap(
                text = context.getString(R.string.timer_exceed_time),
                fontResId = R.font.pretendard_semibold,
                textSize = 18f,
                colorResId = R.color.notification_pomodoro_over_time,
            )
            bitmaps.add(extraTextBitmap)
            totalWidth += extraTextBitmap.width
            maxHeight = maxOf(maxHeight, extraTextBitmap.height)
        }

        return Bitmap.createBitmap(
            totalWidth,
            maxHeight,
            Bitmap.Config.ARGB_8888,
        ).apply {
            val canvas = Canvas(this)
            var currentX = 0f

            bitmaps.forEach { bitmap ->
                canvas.drawBitmap(bitmap, currentX, 0f, null)
                currentX += bitmap.width
            }
        }
    }
}
