package com.pomonyang.data.remote.datasource.auth

import com.pomonyang.data.remote.model.request.RefreshTokenRequest
import com.pomonyang.data.remote.service.AuthService
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun login() {
    }

    override suspend fun logout() {
    }

    override suspend fun refreshAccessToken(refreshToken: String) = authService.refreshToken(RefreshTokenRequest(refreshToken))
}
