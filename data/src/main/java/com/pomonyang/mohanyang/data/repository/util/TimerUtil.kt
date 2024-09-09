package com.pomonyang.mohanyang.data.repository.util

import java.time.Instant
import java.time.format.DateTimeFormatter

internal fun getCurrentIsoInstant(): String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
