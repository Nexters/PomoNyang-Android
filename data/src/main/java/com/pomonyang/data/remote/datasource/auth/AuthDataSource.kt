package com.pomonyang.data.remote.datasource.auth

import com.pomonyang.data.remote.model.response.TokenResponse

interface AuthDataSource {
    suspend fun login() // TODO
    suspend fun logout() // TODO
    suspend fun refreshAccessToken(refreshToken: String): Result<TokenResponse>
}
