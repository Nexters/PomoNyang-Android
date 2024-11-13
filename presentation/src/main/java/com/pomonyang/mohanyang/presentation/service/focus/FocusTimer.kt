package com.pomonyang.mohanyang.presentation.service.focus

import com.pomonyang.mohanyang.presentation.service.BasePomodoroTimer
import javax.inject.Inject

internal class FocusTimer @Inject constructor() : BasePomodoroTimer() {

    override fun getTagName(): String = "TIMER_FOCUS"
}
