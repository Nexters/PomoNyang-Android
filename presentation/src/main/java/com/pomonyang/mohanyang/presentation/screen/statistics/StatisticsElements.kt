package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.statistics.model.FocusTimeModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.RankingItemModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.StatisticsModel
import java.time.Duration
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
data class StatisticsState(
    val isLoading: Boolean = false,
    val targetDate: LocalDate,
    val totalFocusTime: Duration,
    val focusTimes: ImmutableList<FocusTimeModel>,
    val weeklyFocusTimeList: ImmutableList<Float>,
    val weeklyMaxFocusTime: Float,
    val categoryRankingList: ImmutableList<RankingItemModel>,
    val categoryRankingDate: Pair<LocalDate, LocalDate>,
) : ViewState

@Immutable
sealed interface StatisticsEvent : ViewEvent {
    data class Refresh(val date: LocalDate) : StatisticsEvent
    data class ClickNextDay(val nextDay: LocalDate) : StatisticsEvent
    data class ClickPrevDay(val prevDay: LocalDate) : StatisticsEvent
    data class SelectDate(val date: LocalDate) : StatisticsEvent
    data object ShowDatePickerDialog : StatisticsEvent
    data object HideDatePickerDialog : StatisticsEvent
}

@Immutable
sealed interface StatisticsSideEffect : ViewSideEffect {
    data object ShowDatePickerDialog : StatisticsSideEffect
    data object SideDatePickerDialog : StatisticsSideEffect
}

fun StatisticsModel.toState(): StatisticsState = StatisticsState(
    isLoading = false,
    targetDate = date,
    totalFocusTime = totalFocusTime,
    focusTimes = focusTimes.toImmutableList(),
    weeklyFocusTimeList = weeklyFocusTimeTrend.toFixed7DayFocusTrend().toImmutableList(),
    weeklyMaxFocusTime = weeklyFocusTimeTrend.weeklyMaxFocusTime,
    categoryRankingList = categoryRanking.rankingItems.toImmutableList(),
    categoryRankingDate = categoryRanking.startDate to categoryRanking.endDate,
)
