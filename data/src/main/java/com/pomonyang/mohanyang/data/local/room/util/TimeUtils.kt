package com.pomonyang.mohanyang.data.local.room.util

import kotlin.time.Duration.Companion.minutes

fun Int.formatDurationToMinutesString(): String = this.minutes.inWholeMinutes.toString()
