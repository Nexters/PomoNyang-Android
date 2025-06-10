package com.pomonyang.mohanyang.presentation.screen.statistics.component.datepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
data class StatisticDateState(
    val selectedDate: LocalDate,
    val canMoveToPreviousDay: Boolean,
    val canMoveToNextDay: Boolean,
    val selectedDateMillis: Long,
    val selectedMonth: Int,
    val selectedDay: Int,
    val updateDate: (Long) -> Unit,
    val moveToPreviousDay: () -> Unit,
    val moveToNextDay: () -> Unit,
    val isSelectableDate: (Long) -> Boolean,
) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean = isSelectableDate.invoke(utcTimeMillis)
}

@Composable
fun rememberStatisticDateState(
    joinedDate: LocalDate,
    zoneId: ZoneId = ZoneId.systemDefault(),
    nowProvider: () -> LocalDate = { LocalDate.now() },
): StatisticDateState {
    var selectedDate by remember { mutableStateOf(nowProvider()) }

    val now = nowProvider()

    fun isValid(date: LocalDate): Boolean = date.isBefore(joinedDate).not() && date.isAfter(now).not()

    fun updateDate(epochMillis: Long) {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        if (isValid(date)) selectedDate = date
    }

    fun moveToPreviousDay() {
        val prev = selectedDate.minusDays(1)
        if (isValid(prev)) selectedDate = prev
    }

    fun moveToNextDay() {
        val next = selectedDate.plusDays(1)
        if (isValid(next)) selectedDate = next
    }

    return StatisticDateState(
        selectedDate = selectedDate,
        selectedMonth = selectedDate.monthValue,
        selectedDay = selectedDate.dayOfMonth,
        selectedDateMillis = selectedDate.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        canMoveToPreviousDay = selectedDate.minusDays(1).isBefore(joinedDate).not(),
        canMoveToNextDay = selectedDate.plusDays(1).isAfter(now).not(),
        updateDate = ::updateDate,
        moveToPreviousDay = ::moveToPreviousDay,
        moveToNextDay = ::moveToNextDay,
        isSelectableDate = { epochMillis ->
            isValid(Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate())
        },
    )
}
