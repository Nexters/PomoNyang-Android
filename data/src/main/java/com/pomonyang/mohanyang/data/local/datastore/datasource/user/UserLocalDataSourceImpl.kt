package com.pomonyang.mohanyang.data.local.datastore.datasource.user

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pomonyang.mohanyang.data.local.datastore.di.UserDataStore
import com.pomonyang.mohanyang.data.remote.model.response.CatTypeResponse
import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl @Inject constructor(
    @UserDataStore private val dataStore: DataStore<Preferences>,
) : UserLocalDataSource {
    override suspend fun saveUserInfo(userInfo: UserInfoResponse) {
        dataStore.edit { preferences ->
            userInfo.let {
                preferences[booleanPreferencesKey(USER_PUSH_KEY)] = it.isPushEnabled
                preferences[intPreferencesKey(USER_DEVICE_NO_KEY)] = it.registeredDeviceNo
                preferences[intPreferencesKey(USER_CAT_NO_KEY)] = it.cat.no
                preferences[stringPreferencesKey(USER_CAT_TYPE_KEY)] = it.cat.type
                preferences[stringPreferencesKey(USER_CAT_NAME_KEY)] = it.cat.name
            }
        }
    }

    override suspend fun getUserInfo(): UserInfoResponse = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        UserInfoResponse(
            registeredDeviceNo = it[intPreferencesKey(USER_DEVICE_NO_KEY)] ?: -1,
            isPushEnabled = it[booleanPreferencesKey(USER_PUSH_KEY)] ?: false,
            cat = CatTypeResponse(
                no = it[intPreferencesKey(USER_CAT_NO_KEY)] ?: -1,
                type = it[stringPreferencesKey(USER_CAT_TYPE_KEY)] ?: "",
                name = it[stringPreferencesKey(USER_CAT_NAME_KEY)] ?: "",
            ),
        )
    }.first()

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
        private const val USER_PUSH_KEY = "user_push"
        private const val USER_DEVICE_NO_KEY = "user_device_no"
        private const val USER_CAT_NO_KEY = "user_cat_no"
        private const val USER_CAT_TYPE_KEY = "user_cat_type"
        private const val USER_CAT_NAME_KEY = "user_cat_name"
    }
}
