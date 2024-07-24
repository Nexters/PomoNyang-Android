package com.pomonyang.data.local.datastore.di

import com.pomonyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSourceImpl
import com.pomonyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.data.local.datastore.datasource.token.TokenLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {
    @Binds
    abstract fun provideTokenDataSource(tokenDataSourceImpl: TokenLocalDataSourceImpl): TokenLocalDataSource

    @Binds
    abstract fun provideDeviceIdDataSource(deviceIdDataSourceImpl: DeviceIdLocalDataSourceImpl): DeviceIdLocalDataSource
}
