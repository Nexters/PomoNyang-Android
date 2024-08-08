package com.pomonyang.mohanyang.data.repository.cat

import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

class CatSettingRepositoryImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService
) : CatSettingRepository {
    override suspend fun getCatTypes() = mohaNyangService.getCatTypes()
    override suspend fun setCatName(name: String) {
        TODO("Not yet implemented")
    }
}
