package com.pomonyang.data.remote.interceptor

import com.pomonyang.data.local.datastore.datasource.token.TokenDataSource
import com.pomonyang.data.remote.datasource.auth.AuthDataSource
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class TokenRefreshInterceptor @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource
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
        val refreshToken = runBlocking { tokenDataSource.getRefreshToken() }
        val newAccessToken = runBlocking { authDataSource.refreshAccessToken(refreshToken) }.getOrNull()
        return newAccessToken?.let {
            tokenDataSource.saveAccessToken(it.accessToken)
            tokenDataSource.saveRefreshToken(it.refreshToken)
            it.accessToken
        } ?: run {
            authDataSource.logout()
            null
        }
    }
}
