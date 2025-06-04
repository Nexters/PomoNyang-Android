package com.pomonyang.mohanyang.presentation.screen.statistics.model.mapper

import com.pomonyang.mohanyang.data.remote.model.response.CategoryRankingResponse
import com.pomonyang.mohanyang.data.remote.model.response.CategoryResponse
import com.pomonyang.mohanyang.data.remote.model.response.DailyFocusTimeResponse
import com.pomonyang.mohanyang.data.remote.model.response.FocusTimeResponse
import com.pomonyang.mohanyang.data.remote.model.response.RankingItemResponse
import com.pomonyang.mohanyang.data.remote.model.response.StatisticsResponse
import com.pomonyang.mohanyang.data.remote.model.response.WeeklyFocusTimeTrendResponse
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.statistics.model.CategoryModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.CategoryRankingModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.DailyFocusTimeModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.FocusTimeModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.RankingItemModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.StatisticsModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.WeeklyFocusTimeTrendModel
import java.time.Duration
import java.time.LocalDate

fun StatisticsResponse.toModel(): StatisticsModel = StatisticsModel(
    date = LocalDate.parse(this.date),
    totalFocusTime = Duration.parse(this.totalFocusTime),
    focusTimes = this.focusTimes.map { it.toModel() },
    weeklyFocusTimeTrend = this.weeklyFocusTimeTrend.toModel(),
    categoryRanking = this.categoryRanking.toModel(),
)

fun FocusTimeResponse.toModel(): FocusTimeModel = FocusTimeModel(
    no = this.no,
    category = this.category.toModel(),
    totalFocusTime = Duration.parse(this.totalFocusTime),
)

fun CategoryResponse.toModel(): CategoryModel = CategoryModel(
    no = this.no,
    title = this.title,
    icon = CategoryIcon.safeValueOf(this.iconType),
)

fun WeeklyFocusTimeTrendResponse.toModel(): WeeklyFocusTimeTrendModel = WeeklyFocusTimeTrendModel(
    startDate = LocalDate.parse(this.startDate),
    endDate = LocalDate.parse(this.endDate),
    dateToFocusTimeStatistics = this.dateToFocusTimeStatistics.map { it.toModel() },
)

fun DailyFocusTimeResponse.toModel(): DailyFocusTimeModel = DailyFocusTimeModel(
    date = LocalDate.parse(this.date),
    totalFocusTime = Duration.parse(this.totalFocusTime),
)

fun CategoryRankingResponse.toModel(): CategoryRankingModel = CategoryRankingModel(
    startDate = LocalDate.parse(this.startDate),
    endDate = LocalDate.parse(this.endDate),
    rankingItems = this.rankingItems.map { it.toModel() },
)

fun RankingItemResponse.toModel(): RankingItemModel = RankingItemModel(
    rank = this.rank,
    category = this.category.toModel(),
    totalFocusTime = Duration.parse(this.totalFocusTime),
)
