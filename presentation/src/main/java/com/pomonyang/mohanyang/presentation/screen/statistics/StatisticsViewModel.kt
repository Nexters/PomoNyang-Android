package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.statistics.StatisticsRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.StatisticsModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.mapper.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
) : BaseViewModel<StatisticsState, StatisticsEvent, StatisticsSideEffect>() {

    init {
        viewModelScope.launch {
            statisticsRepository.getStatistics(
                LocalDate.now(),
            ).onSuccess { statisticsResponse ->
                updateState {
                    copy(
                        statisticsModel = statisticsResponse.toModel(),
                    )
                }
            }.onFailure { error ->
                Timber.e("getStatistics fail $error")
            }
        }
    }

    override fun setInitialState(): StatisticsState = StatisticsState(
        statisticsModel = StatisticsModel.placeHolder,
    )

    override fun handleEvent(event: StatisticsEvent) {
        when (event) {
            else -> {}
        }
    }
}
