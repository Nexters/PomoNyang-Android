package com.pomonyang.mohanyang.presentation.screen.statistics

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState

data object StatisticsState : ViewState

sealed interface StatisticsEvent : ViewEvent

sealed interface StatisticsSideEffect : ViewSideEffect
