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
    private val deviceIdLocalDataSource: DeviceIdLocalDataSource,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val response = chain.proceed(origin)

        if (response.code == 401) {
            response.close()

            val accessToken = runBlocking { getNewAccessToken() }

            if (accessToken != null) {
                val request =
                    origin
                        .newBuilder()
                        .removeHeader("Authorization")
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                return chain.proceed(request)
            } else {
                /*  Refresh Token 만료로 인한 갱신 에러 발생 시 */
                val refreshedToken = runBlocking { refreshAllTokensWithDeviceId() }

                refreshedToken?.let {
                    val refreshAllTokenRequest = origin
                        .newBuilder()
                        .removeHeader("Authorization").header("Authorization", "Bearer $refreshedToken")
                        .build()
                    return chain.proceed(refreshAllTokenRequest)
                }
            }
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
