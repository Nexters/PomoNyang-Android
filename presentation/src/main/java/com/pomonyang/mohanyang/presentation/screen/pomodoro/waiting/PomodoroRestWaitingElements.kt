package com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState

data class PomodoroRestWaitingState(
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true,
    val forceGoHome: Boolean = false
) : ViewState

sealed interface PomodoroRestWaitingEvent : ViewEvent {
    data class Init(
        val exceedTime: Int,
        val focusTime: Int,
        val type: String
    ) : PomodoroRestWaitingEvent

    data object OnNavigationClick : PomodoroRestWaitingEvent
    data object OnEndFocusClick : PomodoroRestWaitingEvent
    data object OnStartRestClick : PomodoroRestWaitingEvent
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestWaitingEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestWaitingEvent
}

sealed interface PomodoroRestWaitingSideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : PomodoroRestWaitingSideEffect
    data object GoToPomodoroRest : PomodoroRestWaitingSideEffect

    data class ShowSnackbar(
        val message: String,
        @DrawableRes val iconRes: Int
    ) : PomodoroRestWaitingSideEffect
}
