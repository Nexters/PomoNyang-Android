package com.pomonyang.mohanyang.presentation.base

interface ViewState

interface ViewEvent

interface ViewSideEffect


open class NetworkViewState(
    open val isLoading: Boolean = false,
    open val isInternalError: Boolean = false,
    open val isInvalidError: Boolean = false,
    open val lastRequestAction: ViewEvent? = null
) : ViewState
