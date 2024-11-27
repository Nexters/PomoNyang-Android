package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState

data object PomodoroFocusState : ViewState

sealed interface PomodoroFocusEvent : ViewEvent {
    data object ClickRest : PomodoroFocusEvent
    data object ClickHome : PomodoroFocusEvent
    data object Pause : PomodoroFocusEvent
    data object Resume : PomodoroFocusEvent
    data object Dispose : PomodoroFocusEvent
}

sealed interface PomodoroFocusEffect : ViewSideEffect {
    data object GoToPomodoroRest : PomodoroFocusEffect
    data object GoToPomodoroSetting : PomodoroFocusEffect
    data object StartFocusAlarm : PomodoroFocusEffect
    data object StopFocusAlarm : PomodoroFocusEffect
}
