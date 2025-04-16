package com.pomonyang.mohanyang.data.remote.service

import com.pomonyang.mohanyang.data.remote.model.request.RefreshTokenRequest
import com.pomonyang.mohanyang.data.remote.model.request.TokenRequest
import com.pomonyang.mohanyang.data.remote.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/papi/v1/tokens/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest,
    ): Result<TokenResponse>

    @POST("/papi/v1/tokens")
    suspend fun getTokenByDeviceId(
        @Body request: TokenRequest,
    ): Result<TokenResponse>
}
