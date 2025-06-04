package com.pomonyang.mohanyang.data.repository.statistics

import com.pomonyang.mohanyang.data.remote.model.response.StatisticsResponse
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class StatisticsRepositoryImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService,
) : StatisticsRepository {

    override suspend fun getStatistics(date: LocalDate): Result<StatisticsResponse> = runCatching {
        mohaNyangService.getStatistics(date.format(DateTimeFormatter.ISO_DATE))
    }
}
