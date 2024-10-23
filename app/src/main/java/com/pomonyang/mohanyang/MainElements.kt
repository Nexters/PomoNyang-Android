package com.pomonyang.mohanyang

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel

data class MainState(
    val isError: Boolean = false,
    val isLoading: Boolean = true,
) : ViewState

sealed interface MainEvent : ViewEvent {
    data object Init : MainEvent
    data object ClickRefresh : MainEvent
    data object ClickClose : MainEvent
}

sealed interface MainEffect : ViewSideEffect {
    data object ShowDialog : MainEffect
    data object DismissDialog : MainEffect
    data object GoToOnBoarding : MainEffect
    data object GoToTimer : MainEffect
    data object ExitApp : MainEffect
}


