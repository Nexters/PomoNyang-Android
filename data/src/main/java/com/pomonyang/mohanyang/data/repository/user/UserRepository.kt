package com.pomonyang.mohanyang.data.repository.user

import com.pomonyang.mohanyang.data.remote.model.response.TokenResponse

interface UserRepository {
    suspend fun getDeviceId(): String
    fun isNewUser(): Boolean
    suspend fun login(deviceId: String): Result<TokenResponse>
    suspend fun saveToken(accessToken: String, refreshToken: String)
}