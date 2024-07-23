package com.pomonyang.data.remote.interceptor

import com.pomonyang.data.local.datastore.datasource.token.TokenDataSource
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class HttpRequestInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenDataSource.getAccessToken()
        }
        val origin = chain.request()
        val request =
            origin
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        return chain.proceed(request)
    }
}
