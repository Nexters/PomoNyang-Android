package com.pomonyang.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pomonyang.data.BuildConfig
import com.pomonyang.data.remote.interceptor.HttpRequestInterceptor
import com.pomonyang.data.remote.service.PomoNyangService
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
internal object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    /**
     * ## ignoreUnknownKeys = true
     * > 버전이 변경이 되면서 추가되는 값이나 삭제되는 값이 생길 때 dto에서 없어도 정상 동작
     *
     * ## coerceInputValues = true
     * > non-null인데 null 값이 들어올 때 해당 타입의 기본값으로 세팅되게
     *
     * ## isLenient = true
     * > dto에서는 String이지만 Int로 들어올 때 등등 자동으로 파싱
     */
    @Provides
    @Singleton
    fun provideJsonBuilder(): Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
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
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): PomoNyangService = retrofit.create(PomoNyangService::class.java)
}
