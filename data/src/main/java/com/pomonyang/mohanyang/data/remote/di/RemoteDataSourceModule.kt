package com.pomonyang.mohanyang.data.remote.di

import com.pomonyang.mohanyang.data.remote.datasource.auth.AuthRemoteDataSource
import com.pomonyang.mohanyang.data.remote.datasource.auth.AuthRemoteDataSourceImpl
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteDataSourceModule {
    @Binds
    @Singleton
    abstract fun provideAuthDataSource(authDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun providePomodoroSettingDataSource(
        pomodoroSettingRemoteDataSourceImpl: PomodoroSettingRemoteDataSourceImpl
    ): PomodoroSettingRemoteDataSource
}
