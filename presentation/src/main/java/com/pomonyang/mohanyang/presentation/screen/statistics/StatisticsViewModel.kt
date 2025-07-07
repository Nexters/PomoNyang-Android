package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.statistics.StatisticsRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
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
    private val userRepository: UserRepository,
) : BaseViewModel<StatisticsState, StatisticsEvent, StatisticsSideEffect>() {

    init {
        fetchStatistics(LocalDate.now())
    }

    override fun setInitialState(): StatisticsState = StatisticsModel.placeHolder.toState(LocalDate.now())

    override fun handleEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.Refresh -> {
                fetchStatistics(event.date)
            }

            is StatisticsEvent.SelectDate -> {
                fetchStatistics(event.date)
            }

            is StatisticsEvent.ClickNextDay -> {
                fetchStatistics(event.nextDay)
            }

            is StatisticsEvent.ClickPrevDay -> {
                fetchStatistics(event.prevDay)
            }

            StatisticsEvent.ShowDatePickerDialog -> {
                setEffect(StatisticsSideEffect.ShowDatePickerDialog)
            }

            StatisticsEvent.HideDatePickerDialog -> {
                setEffect(StatisticsSideEffect.SideDatePickerDialog)
            }
        }
    }

    private fun fetchStatistics(date: LocalDate) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            statisticsRepository.getStatistics(
                date,
            ).onSuccess { statisticsResponse ->
                userRepository.getJoinedDate().let { joinedDate ->
                    updateState {
                        statisticsResponse.toModel().toState(joinedDate)
                    }
                }
            }.onFailure { error ->
                Timber.e("hyom : getStatistics fail $error")
            }
            updateState { copy(isLoading = false) }
        }
    }
}
