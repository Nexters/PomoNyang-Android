package com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pomonyang.mohanyang.data.local.datastore.di.DeviceIdDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

internal class DeviceIdLocalDataSourceImpl @Inject constructor(
    @DeviceIdDataStore private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context,
) : DeviceIdLocalDataSource {

    override suspend fun getDeviceId(): String = getStoredDeviceId() ?: getSSAID() ?: getUUID()

    private suspend fun getStoredDeviceId(): String? = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[DEVICE_ID_KEY]

    @SuppressLint("HardwareIds")
    private fun getSSAID(): String? = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    private fun getUUID(): String = UUID.randomUUID().toString()

    companion object {
        const val DEVICE_ID_PREFERENCES_NAME = "device_id_preferences"
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }
}
