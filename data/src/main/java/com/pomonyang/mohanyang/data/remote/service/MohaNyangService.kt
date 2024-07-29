package com.pomonyang.mohanyang.data.remote.service

import retrofit2.http.GET

interface MohaNyangService {
    /**
     * SAMPLE
     */
    @GET("/mohaNyang")
    suspend fun getMohaNyang(): Result<String>
}
