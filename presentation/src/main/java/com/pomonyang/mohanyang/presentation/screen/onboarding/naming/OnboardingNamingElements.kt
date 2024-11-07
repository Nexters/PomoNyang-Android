package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect

data class NamingState(
    override val isLoading: Boolean = false,
    override val isInvalidError: Boolean = false,
    override val isInternalError: Boolean = false,
    override val lastRequestAction: NamingEvent? = null
) : NetworkViewState()

sealed interface NamingEvent : ViewEvent {
    data class OnComplete(val name: String) : NamingEvent
    data object OnClickRetry : NamingEvent
}

sealed interface NamingSideEffect : ViewSideEffect {
    data object NavToNext : NamingSideEffect
}

