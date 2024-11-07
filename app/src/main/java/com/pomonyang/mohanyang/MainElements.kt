package com.pomonyang.mohanyang

import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect

data class MainState(
    override val isLoading: Boolean = true,
    override val isInternalError: Boolean = false,
    override val isInvalidError: Boolean = false,
    override val lastRequestAction: MainEvent? = null
) : NetworkViewState()

sealed interface MainEvent : ViewEvent {
    data object Init : MainEvent
    data object ClickRefresh : MainEvent
    data object ClickClose : MainEvent
    data object ClickRetry : MainEvent
}

sealed interface MainEffect : ViewSideEffect {
    data object ShowDialog : MainEffect
    data object DismissDialog : MainEffect
    data object GoToOnBoarding : MainEffect
    data object GoToTimer : MainEffect
    data object ExitApp : MainEffect
}
