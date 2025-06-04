package com.pomonyang.mohanyang.data.repository.statistics

import com.pomonyang.mohanyang.data.remote.model.response.StatisticsResponse
import java.time.LocalDate

interface StatisticsRepository {
    suspend fun getStatistics(date: LocalDate): Result<StatisticsResponse>
}
