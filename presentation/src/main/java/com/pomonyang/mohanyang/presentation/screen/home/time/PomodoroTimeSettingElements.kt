package com.pomonyang.mohanyang.presentation.screen.home.time

import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon

data class PomodoroTimeSettingState(
    val categoryNo: Int = 0,
    val titleName: String = "",
    val initialFocusTime: Int = 10,
    val initialRestTime: Int = 10,
    val pickFocusTime: Int = 10,
    val pickRestTime: Int = 10,
    val isFocus: Boolean = false,
    val categoryIcon: CategoryIcon = CategoryIcon.CAT,
    override val isLoading: Boolean = false,
    override val isInternalError: Boolean = false,
    override val isInvalidError: Boolean = false,
    override val lastRequestAction: PomodoroTimeSettingEvent? = null,
) : NetworkViewState()

sealed interface PomodoroTimeSettingEvent : ViewEvent {
    data class Init(val isFocusTime: Boolean) : PomodoroTimeSettingEvent

    data object Submit : PomodoroTimeSettingEvent

    data class ChangePickTime(
        val time: Int,
    ) : PomodoroTimeSettingEvent

    data object ClickClose : PomodoroTimeSettingEvent

    data object ClickRetry : PomodoroTimeSettingEvent
}

sealed interface PomodoroTimeSettingEffect : ViewSideEffect {
    data object GoToPomodoroSettingScreen : PomodoroTimeSettingEffect

    data object ClosePomodoroTimerSettingScreen : PomodoroTimeSettingEffect
}
