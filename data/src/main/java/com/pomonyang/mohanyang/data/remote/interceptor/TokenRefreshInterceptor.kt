package com.pomonyang.mohanyang.data.remote.interceptor

import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.remote.datasource.auth.AuthRemoteDataSource
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class TokenRefreshInterceptor @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val deviceIdLocalDataSource: DeviceIdLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val response = chain.proceed(origin)

        if (response.code == 401) {
            response.close()

            val accessToken = runBlocking { getNewAccessToken() }
            val request =
                origin
                    .newBuilder()
                    .removeHeader("Authorization")
                    .header("Authorization", "Bearer $accessToken")
                    .build()

            val accessRefreshResponse = chain.proceed(request)

            if (accessRefreshResponse.code == 400) {
                accessRefreshResponse.close()

                val refreshedToken = runBlocking { refreshAllTokensWithDeviceId() }

                if (refreshedToken != null) {
                    val refreshAllTokenRequest = origin
                        .newBuilder()
                        .removeHeader("Authorization").header("Authorization", "Bearer $refreshedToken")
                        .build()

                    return chain.proceed(refreshAllTokenRequest)
                }
            }

            return accessRefreshResponse
        }
        return response
    }

    private suspend fun getNewAccessToken(): String? {
        val refreshToken = runBlocking { tokenLocalDataSource.getRefreshToken() }
        val newAccessToken = runBlocking {
            authRemoteDataSource.refreshAccessToken(refreshToken)
        }.getOrNull()
        return newAccessToken?.let {
            tokenLocalDataSource.saveAccessToken(it.accessToken)
            tokenLocalDataSource.saveRefreshToken(it.refreshToken)
            it.accessToken
        } ?: run {
            authRemoteDataSource.logout()
            null
        }
    }

    private suspend fun refreshAllTokensWithDeviceId(): String? {
        val deviceId = deviceIdLocalDataSource.getDeviceId()
        return authRemoteDataSource.login(deviceId).getOrNull()?.let { tokenData ->
            tokenLocalDataSource.saveAccessToken(tokenData.accessToken)
            tokenLocalDataSource.saveRefreshToken(tokenData.refreshToken)
            tokenData.accessToken
        } ?: run {
            authRemoteDataSource.logout()
            null
        }
    }
}
