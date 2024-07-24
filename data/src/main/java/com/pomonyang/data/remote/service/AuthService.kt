package com.pomonyang.data.remote.service

import com.pomonyang.data.remote.model.request.RefreshTokenRequest
import com.pomonyang.data.remote.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    /*SAMPLE Refresh Access Token API, signin, signout etc..*/
    @POST("/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Result<TokenResponse>
}
