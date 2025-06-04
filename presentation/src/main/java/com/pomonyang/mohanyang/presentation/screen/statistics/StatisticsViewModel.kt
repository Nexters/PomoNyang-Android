package com.pomonyang.mohanyang.presentation.screen.statistics

import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    // TODO Repository 연결
) : BaseViewModel<StatisticsState, StatisticsEvent, StatisticsSideEffect>() {

    override fun setInitialState(): StatisticsState = StatisticsState

    override fun handleEvent(event: StatisticsEvent) {
        when (event) {
            else -> {}
        }
    }
}
