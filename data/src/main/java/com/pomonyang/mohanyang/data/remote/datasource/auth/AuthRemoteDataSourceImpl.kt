package com.pomonyang.mohanyang.data.remote.datasource.auth

import com.pomonyang.mohanyang.data.remote.model.request.RefreshTokenRequest
import com.pomonyang.mohanyang.data.remote.model.request.TokenRequest
import com.pomonyang.mohanyang.data.remote.service.AuthService
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun login(deviceId: String) = authService.getTokenByDeviceId(TokenRequest(deviceId))

    override suspend fun logout() {
    }

    override suspend fun refreshAccessToken(refreshToken: String) = authService.refreshToken(RefreshTokenRequest(refreshToken))
}
