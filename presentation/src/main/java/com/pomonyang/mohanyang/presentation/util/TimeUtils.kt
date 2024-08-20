package com.pomonyang.mohanyang.presentation.util

import java.time.LocalTime
import java.util.*

fun LocalTime.displayAlarm(): String = "${this.hour}".padStart(2, '0') + ":" + "${this.minute}".padStart(2, '0') + " " + if (this.hour < 12) "AM" else "PM"

fun Int.formatTime(): String {
    val minutesPart = this / 60
    val secondsPart = this % 60
    return String.format(Locale.KOREAN, "%02d:%02d", minutesPart, secondsPart)
}

fun Int.formatToMinutesAndSeconds(): String {
    val minutes = this
    val seconds = 0
    return String.format("%02d:%02d", minutes, seconds)
}
