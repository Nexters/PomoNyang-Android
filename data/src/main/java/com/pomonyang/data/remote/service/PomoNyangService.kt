package com.pomonyang.data.remote.service

import retrofit2.http.GET

interface PomoNyangService {
    /**
     * SAMPLE
     */
    @GET("/pomoNyang")
    suspend fun getPomoNyang(): Result<String>
}
