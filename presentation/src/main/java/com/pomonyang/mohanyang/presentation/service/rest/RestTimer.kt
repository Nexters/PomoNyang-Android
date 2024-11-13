package com.pomonyang.mohanyang.presentation.service.rest

import com.pomonyang.mohanyang.presentation.service.BasePomodoroTimer
import javax.inject.Inject

internal class RestTimer @Inject constructor() : BasePomodoroTimer() {

    override fun getTagName(): String = "TIMER_REST"
}
