package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.annotation.DrawableRes
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.setting.PomodoroCategoryType
import com.pomonyang.mohanyang.presentation.screen.pomodoro.focus.PomodoroFocusEvent
import com.pomonyang.mohanyang.presentation.util.formatTime

data class PomodoroRestState(
    val pomodoroId: String = "",
    val remainingRestTime: Int = 0,
    val restExceededTime: Int = 0,
    val maxRestTime: Int = 0,
    val categoryType: PomodoroCategoryType = PomodoroCategoryType.DEFAULT,
    val cat: CatType = CatType.CHEESE,
    val categoryNo: Int = -1,
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true
) : ViewState {
    fun displayRestTime(): String = remainingRestTime.formatTime()
    fun displayRestExceedTime(): String = restExceededTime.formatTime()
}

sealed interface PomodoroRestEvent : ViewEvent {
    data class Init(val pomodoroId: String) : PomodoroRestEvent
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
