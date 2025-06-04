package com.pomonyang.mohanyang.presentation.screen.statistics.component.graph

import java.time.LocalDateTime
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

data class FocusGraphConfigure(
    val targetDateTime: LocalDateTime = LocalDateTime.now(),
    val maxFocusTime: Float,
) {
    private val maxFocusDuration: Duration = maxFocusTime.toInt().minutes

    val yAxisRange: Duration = when {
        maxFocusDuration.inWholeMinutes == 0L -> 10.minutes
        maxFocusDuration < 1.hours -> 15.minutes
        maxFocusDuration < 5.hours -> 1.hours
        maxFocusDuration < 8.hours -> 2.hours
        maxFocusDuration < 20.hours -> 5.hours
        else -> 6.hours
    }

    val maxYAxis: Duration = when {
        maxFocusDuration.inWholeMinutes == 0L -> 10.minutes
        maxFocusDuration < 1.hours -> 1.hours
        maxFocusDuration < 5.hours -> {
            if (maxFocusDuration.inWholeMinutes.toFloat() % HOUR == 0f) {
                (maxFocusDuration.inWholeHours + 1).hours
            } else {
                ceil(maxFocusDuration.inWholeMinutes.toFloat() / HOUR).toInt().hours
            }
        }
        maxFocusDuration < 8.hours -> 8.hours
        maxFocusDuration < 10.hours -> 10.hours
        maxFocusDuration < 15.hours -> 15.hours
        maxFocusDuration < 20.hours -> 20.hours
        else -> 24.hours
    }

    val xAxis: List<LocalDateTime> = generateWeekDates(targetDateTime)

    fun getYLabel(index: Int): String {
        val value = yAxisRange * index
        return if (value >= 1.hours) "${value.inWholeHours}h" else "${value.inWholeMinutes}m"
    }

    private fun generateWeekDates(baseDate: LocalDateTime): List<LocalDateTime> = (6 downTo 0).map { baseDate.minusDays(it.toLong()) }

    companion object {
        private const val HOUR = 60
    }
}
