package com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.pomonyang.mohanyang.data.local.datastore.di.DeviceIdDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class PomodoroLocalDataSourceImpl @Inject constructor(
    @DeviceIdDataStore private val dataStore: DataStore<Preferences>
) : PomodoroLocalDataSource {

    override fun getRecentCategoryNo(): Flow<Int> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[POMODORO_RECENT_USE_CATEGORY_NO] ?: 0 }

    override suspend fun updateRecentCategoryNo(categoryNo: Int) {
        dataStore.edit { preferences ->
            preferences[POMODORO_RECENT_USE_CATEGORY_NO] = categoryNo
        }
    }

    companion object {
        const val POMODORO_PREFERENCES_NAME = "pomodoro_preferences"
        private val POMODORO_RECENT_USE_CATEGORY_NO = intPreferencesKey("pomodoro_recent_category_no")
    }
}
