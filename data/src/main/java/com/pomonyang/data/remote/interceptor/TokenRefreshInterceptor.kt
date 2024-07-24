package com.pomonyang.data.remote.interceptor

import com.pomonyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.data.remote.datasource.auth.AuthRemoteDataSource
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class TokenRefreshInterceptor @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val response = chain.proceed(origin)
        if (response.code == 401) {
            val accessToken = runBlocking { getNewAccessToken() }
            response.close()
            val request =
                origin
                    .newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "*/*")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            return chain.proceed(request)
        }
        return response
    }

    private suspend fun getNewAccessToken(): String? {
        val refreshToken = runBlocking { tokenLocalDataSource.getRefreshToken() }
        val newAccessToken = runBlocking { authRemoteDataSource.refreshAccessToken(refreshToken) }.getOrNull()
        return newAccessToken?.let {
            tokenLocalDataSource.saveAccessToken(it.accessToken)
            tokenLocalDataSource.saveRefreshToken(it.refreshToken)
            it.accessToken
        } ?: run {
            authRemoteDataSource.logout()
            null
        }
    }
}
