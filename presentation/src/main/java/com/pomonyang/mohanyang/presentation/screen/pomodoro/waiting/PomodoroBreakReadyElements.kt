package com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon

data class PomodoroBreakReadyState(
    val pomodoroId: String = "",
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true,
    val forceGoHome: Boolean = false,
    val exceededTime: Int = 0,
    val focusedTime: Int = 0,
    val type: String = "",
    val categoryIcon: CategoryIcon = CategoryIcon.CAT,
) : ViewState

sealed interface PomodoroBreakReadyEvent : ViewEvent {
    data class Init(
        val exceededTime: Int,
        val focusTime: Int,
        val type: String,
        val pomodoroId: String,
    ) : PomodoroBreakReadyEvent

    data object OnNavigationClick : PomodoroBreakReadyEvent
    data object OnEndFocusClick : PomodoroBreakReadyEvent
    data object OnStartRestClick : PomodoroBreakReadyEvent
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroBreakReadyEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroBreakReadyEvent
}

sealed interface PomodoroBreakReadySideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : PomodoroBreakReadySideEffect
    data object GoToPomodoroRest : PomodoroBreakReadySideEffect

    data class ShowSnackbar(
        val message: String,
        @DrawableRes val iconRes: Int,
    ) : PomodoroBreakReadySideEffect
}
