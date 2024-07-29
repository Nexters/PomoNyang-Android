package com.pomonyang.mohanyang.data.remote.datasource.auth

import com.pomonyang.mohanyang.data.remote.model.response.TokenResponse

interface AuthRemoteDataSource {
    suspend fun login() // TODO
    suspend fun logout() // TODO
    suspend fun refreshAccessToken(refreshToken: String): Result<TokenResponse>
}
