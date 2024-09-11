package com.pomonyang.mohanyang.data.remote.interceptor

import android.content.Context
import android.os.Build
import com.mohanyang.data.BuildConfig
import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class HttpRequestInterceptor @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val deviceIdLocalDataSource: DeviceIdLocalDataSource,
    private val context: Context
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
                .header("User-Agent", getUserAgent())
                .build()
        return chain.proceed(request)
    }

    private fun getUserAgent(): String {
        val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
        val version = packageInfo?.versionName ?: ""

        val finalVersion = if (version.split("\\.").size > 3) {
            version.substring(0, version.lastIndexOf("."))
        } else {
            version
        }

        val deviceId = runBlocking {
            deviceIdLocalDataSource.getDeviceId()
        }

        return String.format(
            Locale.KOREA,
            "%s/%s/%s (Android %s, %s, %s, %s)",
            BuildConfig.BUILD_TYPE,
            finalVersion,
            deviceId,
            Build.VERSION.RELEASE,
            Build.MODEL,
            Build.BRAND,
            Locale.getDefault().language
        )
    }
}
