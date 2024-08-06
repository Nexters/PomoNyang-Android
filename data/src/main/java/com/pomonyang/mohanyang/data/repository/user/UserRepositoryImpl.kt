package com.pomonyang.mohanyang.data.repository.user

import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.remote.model.request.TokenRequest
import com.pomonyang.mohanyang.data.remote.service.AuthService
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

internal class UserRepositoryImpl @Inject constructor(
    private val deviceLocalDataStore: DeviceIdLocalDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val authService: AuthService
) : UserRepository {
    override suspend fun getDeviceId() = deviceLocalDataStore.getDeviceId()

    override fun isNewUser(): Boolean = runBlocking { tokenLocalDataSource.getAccessToken().isEmpty() }

    override suspend fun login(deviceId: String) = authService.getTokenByDeviceId(TokenRequest(deviceId))

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        tokenLocalDataSource.saveAccessToken(accessToken)
        tokenLocalDataSource.saveRefreshToken(refreshToken)
    }
}
