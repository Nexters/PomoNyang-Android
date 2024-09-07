package com.pomonyang.mohanyang.data.local.room.util

import java.time.Duration

fun Int.formatDurationToMinutesString(): String = Duration.ofMinutes((this / 60).toLong()).toString()
