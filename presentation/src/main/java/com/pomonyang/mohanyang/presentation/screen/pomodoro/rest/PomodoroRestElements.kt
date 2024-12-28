package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState

data class PomodoroRestState(
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true
) : ViewState

sealed interface PomodoroRestEvent : ViewEvent {
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestEvent
    data object OnEndPomodoroClick : PomodoroRestEvent
    data object OnFocusClick : PomodoroRestEvent
}

sealed interface PomodoroRestEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String, @DrawableRes val iconRes: Int) : PomodoroRestEffect
    data object GoToHome : PomodoroRestEffect
    data object GoToPomodoroFocus : PomodoroRestEffect
}
