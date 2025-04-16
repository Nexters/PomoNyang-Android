package com.pomonyang.mohanyang.data.remote.di

import android.content.Context
import com.datadog.android.okhttp.DatadogEventListener
import com.datadog.android.okhttp.DatadogInterceptor
import com.datadog.android.okhttp.trace.TracingInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mohanyang.data.BuildConfig
import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.remote.datasource.auth.AuthRemoteDataSource
import com.pomonyang.mohanyang.data.remote.interceptor.HttpRequestInterceptor
import com.pomonyang.mohanyang.data.remote.interceptor.TokenRefreshInterceptor
import com.pomonyang.mohanyang.data.remote.service.AuthService
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitorImpl
import com.pomonyang.mohanyang.data.remote.util.NetworkResultCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun bindNetworkConnectivityManager(networkMonitorImpl: NetworkMonitorImpl): NetworkMonitor

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private val tracedHosts = listOf(BASE_URL)

        @Provides
        @Singleton
        fun provideJsonBuilder(): Json = Json {
            ignoreUnknownKeys = true // dto 정의되어 있지 않은 필드도 허락
            coerceInputValues = true // 해당 타입의 기본값으로 세팅되게
            isLenient = true // Json 규격에 안맞게 ex) "" 없이 들어온 경우에도 파싱 가능
            if (BuildConfig.DEBUG) prettyPrint = true
        }

        @Singleton
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("ApiService").d(message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        @Provides
        @Singleton
        fun provideHTTPRequestInterceptor(
            tokenLocalDataSource: TokenLocalDataSource,
            deviceIdLocalDataSource: DeviceIdLocalDataSource,
            @ApplicationContext context: Context,
        ): HttpRequestInterceptor = HttpRequestInterceptor(tokenLocalDataSource, deviceIdLocalDataSource, context)

        @Provides
        @Singleton
        fun provideTokenRefreshInterceptor(
            authRemoteDataSource: AuthRemoteDataSource,
            tokenLocalDataSource: TokenLocalDataSource,
            deviceIdLocalDataSource: DeviceIdLocalDataSource,
        ): TokenRefreshInterceptor = TokenRefreshInterceptor(authRemoteDataSource, tokenLocalDataSource, deviceIdLocalDataSource)

        @Provides
        @Singleton
        @DefaultClient
        fun provideOkHttpClient(
            httpRequestInterceptor: HttpRequestInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(httpRequestInterceptor)
            .applyDatadogInterceptors(tracedHosts)
            .build()

        @Provides
        @Singleton
        @TokenClient
        fun provideTokenOkHttpClient(
            httpRequestInterceptor: HttpRequestInterceptor,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            tokenRefreshInterceptor: TokenRefreshInterceptor,
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenRefreshInterceptor)
            .addInterceptor(httpRequestInterceptor)
            .applyDatadogInterceptors(tracedHosts)
            .build()

        private fun OkHttpClient.Builder.applyDatadogInterceptors(tracedHosts: List<String>): OkHttpClient.Builder = this
            .addInterceptor(DatadogInterceptor.Builder(tracedHosts).build())
            .addNetworkInterceptor(TracingInterceptor.Builder(tracedHosts).build())
            .eventListenerFactory(DatadogEventListener.Factory())

        @Provides
        @Singleton
        fun provideNetworkResultCallAdapter(): NetworkResultCallAdapterFactory = NetworkResultCallAdapterFactory()

        @Provides
        @Singleton
        @DefaultApi
        fun provideRetrofit(
            @DefaultClient okHttpClient: OkHttpClient,
            networkResultCallAdapterFactory: NetworkResultCallAdapterFactory,
            json: Json,
        ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(networkResultCallAdapterFactory)
            .build()

        @Provides
        @Singleton
        @TokenApi
        fun provideTokenInterceptorRetrofit(
            @TokenClient okHttpClient: OkHttpClient,
            networkResultCallAdapterFactory: NetworkResultCallAdapterFactory,
            json: Json,
        ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(networkResultCallAdapterFactory)
            .build()

        @Provides
        @Singleton
        fun provideApiService(
            @TokenApi retrofit: Retrofit,
        ): MohaNyangService = retrofit.create(MohaNyangService::class.java)

        @Provides
        @Singleton
        fun provideAuthApiService(
            @DefaultApi retrofit: Retrofit,
        ): AuthService = retrofit.create(AuthService::class.java)
    }
}
