package com.pomonyang.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pomonyang.data.BuildConfig
import com.pomonyang.data.remote.interceptor.HttpRequestInterceptor
import com.pomonyang.data.remote.service.PomoNyangService
import com.pomonyang.data.remote.util.NetworkConnectivityManager
import com.pomonyang.data.remote.util.NetworkConnectivityManagerImpl
import com.pomonyang.data.remote.util.NetworkResultCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun bindNetworkConnectivityManager(networkConnectivityManagerImpl: NetworkConnectivityManagerImpl): NetworkConnectivityManager

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL

        @Provides
        @Singleton
        fun provideJsonBuilder(): Json =
            Json {
                ignoreUnknownKeys = true // dto 정의되어 있지 않은 필드도 허락
                coerceInputValues = true // 해당 타입의 기본값으로 세팅되게
                isLenient = true // String이지만 Int로 들어올 때 등등 자동으로 파싱
                if (BuildConfig.DEBUG) prettyPrint = true
            }

        @Provides
        @Singleton
        fun provideHTTPRequestInterceptor(): HttpRequestInterceptor = HttpRequestInterceptor()

        @Provides
        @Singleton
        fun provideOkHttpClient(httpRequestInterceptor: HttpRequestInterceptor): OkHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(httpRequestInterceptor)
                .build()

        @Provides
        @Singleton
        fun provideNetworkResultCallAdapter(): NetworkResultCallAdapterFactory = NetworkResultCallAdapterFactory()

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            networkResultCallAdapterFactory: NetworkResultCallAdapterFactory,
            json: Json
        ): Retrofit =
            Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .addCallAdapterFactory(networkResultCallAdapterFactory)
                .build()

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): PomoNyangService = retrofit.create(PomoNyangService::class.java)
    }
}
