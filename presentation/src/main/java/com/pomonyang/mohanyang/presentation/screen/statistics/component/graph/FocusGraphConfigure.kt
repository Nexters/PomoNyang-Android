package com.pomonyang.mohanyang.presentation.screen.statistics.component.graph

import java.time.LocalDateTime
import kotlin.math.ceil

data class FocusGraphConfigure(
    val targetDateTime: LocalDateTime = LocalDateTime.now(),
    val maxFocusTime: Float,
) {
    val yAxisRange: Int = when {
        maxFocusTime == 0.0F -> 10
        maxFocusTime > 0.0F && maxFocusTime < 1.0F * hour -> 15
        maxFocusTime >= 1.0F * hour && maxFocusTime < 5.0F * hour -> 1 * hour
        maxFocusTime >= 5.0F * hour && maxFocusTime < 8.0F * hour -> 2 * hour
        maxFocusTime >= 8.0F * hour && maxFocusTime < 20.0F * hour -> 5 * hour
        else -> 6 * hour
    }

    val maxYAxis = when {
        maxFocusTime == 0.0F -> 10
        maxFocusTime > 0.0F && maxFocusTime < hour -> hour
        maxFocusTime >= hour && maxFocusTime < 5 * hour -> {
            if (maxFocusTime % hour == 0F) {
                maxFocusTime.toInt() + hour
            } else {
                ceil(maxFocusTime / hour).toInt() * hour
            }
        }

        maxFocusTime >= 5 * hour && maxFocusTime < 8 * hour -> 8 * hour
        maxFocusTime >= 8 * hour && maxFocusTime < 20 * hour -> when {
            maxFocusTime < 10 * hour -> 10 * hour
            maxFocusTime < 15 * hour -> 15 * hour
            else -> 20 * hour
        }

        else -> 24 * hour
    }

    val xAxis: List<LocalDateTime> = generateWeekDates(targetDateTime)

    fun getYLabel(index: Int): String {
        val value = index * yAxisRange
        return if (yAxisRange >= hour) "${value / hour}$yAxisUnit" else "$value$yAxisUnit"
    }


    private val yAxisUnit: String = if (maxFocusTime in 0.0F..<1.0F * hour) "m" else "h"

    private fun generateWeekDates(baseDate: LocalDateTime): List<LocalDateTime> =
        (6 downTo 0).map { baseDate.minusDays(it.toLong()) }

    companion object{
        private const val hour = 60
    }
}
