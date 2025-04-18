package com.pomonyang.mohanyang.data.local.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.notification.NotificationLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSourceImpl
import com.pomonyang.mohanyang.data.local.datastore.datasource.user.UserLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @TokenDataStore
    @Provides
    @Singleton
    internal fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = {
            context.preferencesDataStoreFile(
                TokenLocalDataSourceImpl.TOKEN_PREFERENCES_NAME,
            )
        },
    )

    @DeviceIdDataStore
    @Provides
    @Singleton
    internal fun provideDeviceIdDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = {
            context.preferencesDataStoreFile(
                DeviceIdLocalDataSourceImpl.DEVICE_ID_PREFERENCES_NAME,
            )
        },
    )

    @UserDataStore
    @Provides
    @Singleton
    internal fun provideUserDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = {
            context.preferencesDataStoreFile(
                UserLocalDataSourceImpl.USER_PREFERENCES_NAME,
            )
        },
    )

    @NotificationDataStore
    @Provides
    @Singleton
    internal fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() },
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = {
            context.preferencesDataStoreFile(
                NotificationLocalDataSourceImpl.NOTIFICATION_PREFERENCES_NAME,
            )
        },
    )
}
