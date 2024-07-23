package com.pomonyang.data.remote.datasource.auth

import com.pomonyang.data.remote.model.request.RefreshTokenRequest
import com.pomonyang.data.remote.service.AuthService
import javax.inject.Inject

internal class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {

    override suspend fun login() {
    }

    override suspend fun logout() {
    }

    override suspend fun refreshAccessToken(refreshToken: String) = authService.refreshToken(RefreshTokenRequest(refreshToken))
}
