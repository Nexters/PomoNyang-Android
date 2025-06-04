package com.pomonyang.mohanyang.presentation.screen.statistics.model

import androidx.compose.runtime.Stable
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import java.time.Duration
import java.time.LocalDate

@Stable
data class StatisticsModel(
    val date: LocalDate,
    val totalFocusTime: Duration,
    val focusTimes: List<FocusTimeModel>,
    val weeklyFocusTimeTrend: WeeklyFocusTimeTrendModel,
    val categoryRanking: CategoryRankingModel,
) {
    companion object {
        val placeHolder = StatisticsModel(
            date = LocalDate.now(),
            totalFocusTime = Duration.ZERO,
            focusTimes = emptyList(),
            weeklyFocusTimeTrend = WeeklyFocusTimeTrendModel(
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                dateToFocusTimeStatistics = emptyList(),
            ),
            categoryRanking = CategoryRankingModel(
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                rankingItems = emptyList(),
            ),
        )
    }
}

@Stable
data class FocusTimeModel(
    val no: Int,
    val category: PomodoroCategoryModel,
    val totalFocusTime: Duration,
)

@Stable
data class WeeklyFocusTimeTrendModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dateToFocusTimeStatistics: List<DailyFocusTimeModel>,
)

@Stable
data class DailyFocusTimeModel(
    val date: LocalDate,
    val totalFocusTime: Duration,
)

@Stable
data class CategoryRankingModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val rankingItems: List<RankingItemModel>,
)

@Stable
data class RankingItemModel(
    val rank: Int,
    val category: PomodoroCategoryModel,
    val totalFocusTime: Duration,
)
