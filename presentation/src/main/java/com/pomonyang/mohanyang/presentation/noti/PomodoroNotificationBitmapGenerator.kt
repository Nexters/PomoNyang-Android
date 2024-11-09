package com.pomonyang.mohanyang.presentation.noti

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mohanyang.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PomodoroNotificationBitmapGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val timeBitmaps: Map<String, Bitmap> by lazy {
        generateNumberBitmaps(R.font.pretendard_bold, 40f, R.color.notification_pomodoro_time)
    }

    private val overtimeNumberBitmaps: Map<String, Bitmap> by lazy {
        generateNumberBitmaps(R.font.pretendard_semibold, 18f, R.color.notification_pomodoro_over_time)
    }

    private val colonBitmap: Bitmap by lazy {
        createColonBitmap(R.font.pretendard_bold, 40f, R.color.notification_pomodoro_time)
    }

    private val overtimeColonBitmap: Bitmap by lazy {
        createColonBitmap(R.font.pretendard_semibold, 18f, R.color.notification_pomodoro_over_time)
    }

    fun createStatusBitmap(statusText: String): Bitmap = textToBitmap(
        statusText,
        ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!,
        16f,
        ContextCompat.getColor(context, R.color.notification_pomodoro_category_text)
    )

    fun combineTimeBitmaps(time: String, isOvertime: Boolean): Bitmap {
        val digits = time.toCharArray()
        return combineBitmaps(digits, isOvertime)
    }

    private fun generateNumberBitmaps(fontResId: Int, textSize: Float, colorResId: Int): Map<String, Bitmap> {
        val typeface = ResourcesCompat.getFont(context, fontResId)!!
        val textColor = ContextCompat.getColor(context, colorResId)
        return (0..9).associate { number ->
            number.toString() to textToBitmap(
                number.toString(),
                typeface,
                textSize,
                textColor
            )
        }
    }

    private fun createColonBitmap(fontResId: Int, textSize: Float, colorResId: Int): Bitmap {
        val typeface = ResourcesCompat.getFont(context, fontResId)!!
        val textColor = ContextCompat.getColor(context, colorResId)
        return textToBitmap(":", typeface, textSize, textColor)
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
            val extraTextBitmap = textToBitmap(
                context.getString(R.string.timer_exceed_time),
                ResourcesCompat.getFont(context, R.font.pretendard_semibold)!!,
                18f,
                ContextCompat.getColor(context, R.color.notification_pomodoro_over_time)
            )
            bitmaps.add(extraTextBitmap)
            totalWidth += extraTextBitmap.width
            maxHeight = maxOf(maxHeight, extraTextBitmap.height)
        }

        return Bitmap.createBitmap(totalWidth, maxHeight, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            var currentX = 0f

            bitmaps.forEach { bitmap ->
                canvas.drawBitmap(bitmap, currentX, 0f, null)
                currentX += bitmap.width
            }
        }
    }
}
