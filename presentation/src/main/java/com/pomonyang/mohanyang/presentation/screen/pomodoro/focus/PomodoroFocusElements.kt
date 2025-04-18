package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.util.formatTime
import java.util.*

data class PomodoroFocusState(
    val pomodoroId: String = UUID.randomUUID().toString(),
    val remainingFocusTime: Int = 0,
    val focusExceededTime: Int = 0,
    val maxFocusTime: Int = 0,
    val title: String = "",
    val categoryIcon: CategoryIcon = CategoryIcon.CAT,
    val cat: CatType = CatType.CHEESE,
    val categoryNo: Int = -1,
    val forceGoRest: Boolean = false,
) : ViewState {

    fun displayFocusTime(): String = remainingFocusTime.formatTime()
    fun displayFocusExceedTime(): String = focusExceededTime.formatTime()

    val currentFocusTime: Int
        get() = maxFocusTime - remainingFocusTime
}

sealed interface PomodoroFocusEvent : ViewEvent {
    data object Init : PomodoroFocusEvent
    data object ClickRest : PomodoroFocusEvent
    data object ClickHome : PomodoroFocusEvent
}

sealed interface PomodoroFocusEffect : ViewSideEffect {
    data object GoToPomodoroRest : PomodoroFocusEffect
    data object GoToPomodoroSetting : PomodoroFocusEffect
    data object StartFocusAlarm : PomodoroFocusEffect
    data object StopFocusAlarm : PomodoroFocusEffect
}
