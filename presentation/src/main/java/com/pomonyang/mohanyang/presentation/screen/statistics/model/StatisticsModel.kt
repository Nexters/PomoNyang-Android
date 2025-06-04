package com.pomonyang.mohanyang.presentation.screen.statistics.model

import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import java.time.Duration
import java.time.LocalDate

data class StatisticsModel(
    val date: LocalDate,
    val totalFocusTime: Duration,
    val focusTimes: List<FocusTimeModel>,
    val weeklyFocusTimeTrend: WeeklyFocusTimeTrendModel,
    val categoryRanking: CategoryRankingModel,
)

data class FocusTimeModel(
    val no: Int,
    val category: CategoryModel,
    val totalFocusTime: Duration,
)

data class CategoryModel(
    val no: Int,
    val title: String,
    val icon: CategoryIcon,
)

data class WeeklyFocusTimeTrendModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dateToFocusTimeStatistics: List<DailyFocusTimeModel>,
)

data class DailyFocusTimeModel(
    val date: LocalDate,
    val totalFocusTime: Duration,
)

data class CategoryRankingModel(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val rankingItems: List<RankingItemModel>,
)

data class RankingItemModel(
    val rank: Int,
    val category: CategoryModel,
    val totalFocusTime: Duration,
)
