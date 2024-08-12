package com.pomonyang.mohanyang.data.repository.cat

import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse

interface CatSettingRepository {
    suspend fun getCatTypes(): Result<List<CatTypeResponse>>
    suspend fun updateCatInfo(name: String): Result<Unit>
    suspend fun updateCatType(catNo: Int): Result<Unit>
}
