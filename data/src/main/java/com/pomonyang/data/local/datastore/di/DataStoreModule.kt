package com.pomonyang.data.local.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.pomonyang.data.BuildConfig
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
    private const val POMONYANG_PREFERENCES = BuildConfig.PREFERENCE_DATASTORE_NAME

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler =
                ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
            migrations =
                listOf(
                    SharedPreferencesMigration(
                        context,
                        POMONYANG_PREFERENCES
                    )
                ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile =
                {
                    context.preferencesDataStoreFile(
                        POMONYANG_PREFERENCES
                    )
                }
        )
}
