package com.pomonyang.mohanyang.presentation.util

import java.time.LocalTime

fun LocalTime.displayAlarm(): String = "${this.hour}".padStart(2, '0') + ":" + "${this.minute}".padStart(2, '0') + " " + if (this.hour < 12) "AM" else "PM"
