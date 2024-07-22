package com.pomonyang.data.local.datastore.di

import com.pomonyang.data.local.datastore.datasource.deviceid.DeviceIdDataSource
import com.pomonyang.data.local.datastore.datasource.deviceid.DeviceIdDataSourceImpl
import com.pomonyang.data.local.datastore.datasource.token.TokenDataSource
import com.pomonyang.data.local.datastore.datasource.token.TokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    abstract fun provideTokenDataSource(tokenDataSourceImpl: TokenDataSourceImpl): TokenDataSource

    @Binds
    abstract fun provideDeviceIdDataSource(deviceIdDataSourceImpl: DeviceIdDataSourceImpl): DeviceIdDataSource
}
