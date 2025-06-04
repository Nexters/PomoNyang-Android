package com.pomonyang.mohanyang.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponse(
    @SerialName("date")
    val date: String,
    @SerialName("totalFocusTime")
    val totalFocusTime: String,
    @SerialName("focusTimes")
    val focusTimes: List<FocusTimeResponse>,
    @SerialName("weaklyFocusTimeTrend")
    val weeklyFocusTimeTrend: WeeklyFocusTimeTrendResponse,
    @SerialName("categoryRanking")
    val categoryRanking: CategoryRankingResponse,
)

@Serializable
data class FocusTimeResponse(
    @SerialName("no")
    val no: Int,
    @SerialName("category")
    val category: CategoryResponse,
    @SerialName("totalFocusTime")
    val totalFocusTime: String,
)

@Serializable
data class CategoryResponse(
    @SerialName("no")
    val no: Int,
    @SerialName("title")
    val title: String,
    @SerialName("iconType")
    val iconType: String,
)

@Serializable
data class WeeklyFocusTimeTrendResponse(
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("dateToFocusTimeStatistics")
    val dateToFocusTimeStatistics: List<DailyFocusTimeResponse>,
)

@Serializable
data class DailyFocusTimeResponse(
    @SerialName("date")
    val date: String,
    @SerialName("totalFocusTime")
    val totalFocusTime: String,
)

@Serializable
data class CategoryRankingResponse(
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("rankingItems")
    val rankingItems: List<RankingItemResponse>,
)

@Serializable
data class RankingItemResponse(
    @SerialName("rank")
    val rank: Int,
    @SerialName("category")
    val category: CategoryResponse,
    @SerialName("totalFocusTime")
    val totalFocusTime: String,
)
