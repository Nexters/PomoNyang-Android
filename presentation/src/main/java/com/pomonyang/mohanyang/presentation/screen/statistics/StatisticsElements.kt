package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.statistics.model.StatisticsModel

@Stable
data class StatisticsState(
    val statisticsModel: StatisticsModel,
) : ViewState

@Immutable
sealed interface StatisticsEvent : ViewEvent

@Immutable
sealed interface StatisticsSideEffect : ViewSideEffect
