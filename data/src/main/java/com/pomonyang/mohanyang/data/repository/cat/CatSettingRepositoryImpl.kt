package com.pomonyang.mohanyang.data.repository.cat

import com.pomonyang.mohanyang.data.remote.model.request.UpdateCatInfoRequest
import com.pomonyang.mohanyang.data.remote.model.request.UpdateCatTypeRequest
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

class CatSettingRepositoryImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService
) : CatSettingRepository {
    override suspend fun getCatTypes() = mohaNyangService.getCatTypes()

    override suspend fun updateCatInfo(name: String) = mohaNyangService.updateCatInfo(UpdateCatInfoRequest(name))

    override suspend fun updateCatType(catNo: Int) = mohaNyangService.updateCatType(UpdateCatTypeRequest(catNo))
}
