package com.pomonyang.mohanyang.data.local.datastore.datasource.token

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pomonyang.mohanyang.data.local.datastore.di.TokenDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

internal class TokenLocalDataSourceImpl @Inject constructor(
    @TokenDataStore private val dataStore: DataStore<Preferences>,
) : TokenLocalDataSource {

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { preferences ->
            preferences[FCM_TOKEN_KEY] = fcmToken
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = ""
            preferences[REFRESH_TOKEN_KEY] = ""
        }
    }

    override suspend fun getAccessToken(): String = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.first()[ACCESS_TOKEN_KEY] ?: ""

    override suspend fun getRefreshToken(): String = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.first()[REFRESH_TOKEN_KEY] ?: ""

    override suspend fun getFcmToken(): String = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.first()[FCM_TOKEN_KEY] ?: ""

    companion object {
        const val TOKEN_PREFERENCES_NAME = "token_preferences"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }
}
