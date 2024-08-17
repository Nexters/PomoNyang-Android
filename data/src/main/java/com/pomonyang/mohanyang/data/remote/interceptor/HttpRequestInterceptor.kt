package com.pomonyang.mohanyang.data.remote.interceptor

import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class HttpRequestInterceptor @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenLocalDataSource.getAccessToken()
        }
        val origin = chain.request()
        val request =
            origin
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "*/*")
                .removeHeader("Authorization")
                .header("Authorization", "Bearer $accessToken")
                .build()
        return chain.proceed(request)
    }
}
