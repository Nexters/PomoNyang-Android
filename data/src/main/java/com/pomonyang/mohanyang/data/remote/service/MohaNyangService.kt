package com.pomonyang.mohanyang.data.remote.service

import com.pomonyang.mohanyang.data.remote.model.request.UpdateCategoryInfoRequest
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import retrofit2.http.Body
import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MohaNyangService {
    @GET("/api/v1/categories")
    suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>>

    @GET("/api/v1/categories/{no}")
    suspend fun getMohaNyang(
        @Path("no") no: Int
    ): Result<PomodoroSettingResponse>

    @PUT("/api/v1/categories/{no}")
    suspend fun updatePomodoroSetting(
        @Path("no") no: Int,
        @Body updateCategoryInfoRequest: UpdateCategoryInfoRequest
    ): Result<Unit>

    @GET("/api/v1/cats")
    suspend fun getCatTypes(): Result<List<CatTypeResponse>>
}
