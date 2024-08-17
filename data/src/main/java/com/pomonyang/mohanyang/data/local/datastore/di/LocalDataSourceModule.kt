package com.pomonyang.mohanyang.data.local.datastore.di

import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.notification.NotificationLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.notification.NotificationLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro.PomodoroLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro.PomodoroLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.user.UserLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.user.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun provideTokenDataSource(tokenDataSourceImpl: TokenLocalDataSourceImpl): TokenLocalDataSource

    @Binds
    @Singleton
    abstract fun provideDeviceIdDataSource(deviceIdDataSourceImpl: DeviceIdLocalDataSourceImpl): DeviceIdLocalDataSource

    @Binds
    @Singleton
    abstract fun providePomodoroDataSource(pomodoroLocalDataSourceImpl: PomodoroLocalDataSourceImpl): PomodoroLocalDataSource

    @Binds
    @Singleton
    abstract fun provideUserDataSource(userLocalDataSourceImpl: UserLocalDataSourceImpl): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun provideNotificationDataSource(notificationDataSourceImpl: NotificationLocalDataSourceImpl): NotificationLocalDataSource
}
