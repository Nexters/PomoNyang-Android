package com.pomonyang.mohanyang.presentation.screen.pomodoro

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.util.formatTime

data class PomodoroTimerState(
    val remainingFocusTime: Int = 0,
    val focusExceededTime: Int = 0,
    val maxFocusTime: Int = 0,
    val remainingRestTime: Int = 0,
    val restExceededTime: Int = 0,
    val maxRestTime: Int = 0,
    val title: String = "",
    val categoryType: PomodoroCategoryType = PomodoroCategoryType.DEFAULT,
    val cat: CatType = CatType.CHEESE,
    val categoryNo: Int = -1,
    val forceGoRest: Boolean = false
) : ViewState {
    fun displayFocusTime(): String = remainingFocusTime.formatTime()
    fun displayRestTime(): String = remainingRestTime.formatTime()
    fun displayFocusExceedTime(): String = focusExceededTime.formatTime()
    fun displayRestExceedTime(): String = restExceededTime.formatTime()

    val currentFocusTime: Int
        get() = maxFocusTime - remainingFocusTime
}

sealed interface PomodoroTimerEvent : ViewEvent {
    // 포커스, 휴식 이벤트 분리를 했는데 나중에 필요하면 상속받고 추가
    sealed interface PomodoroFocusEvent : PomodoroTimerEvent
    sealed interface PomodoroRestEvent : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    sealed interface PomodoroFocusEffect : PomodoroTimerEffect
    sealed interface PomodoroRestEffect : PomodoroTimerEffect
}
