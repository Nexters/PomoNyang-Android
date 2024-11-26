package com.pomonyang.mohanyang.data.local.datastore.datasource.notification

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.pomonyang.mohanyang.data.local.datastore.di.NotificationDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

class NotificationLocalDataSourceImpl @Inject constructor(
    @NotificationDataStore private val dataStore: DataStore<Preferences>
) : NotificationLocalDataSource {
    override suspend fun saveInterruptNotification(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[INTERRUPT_NOTIFICATION_KEY] = isEnabled
        }
    }

    override suspend fun saveTimerNotification(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[TIMER_NOTIFICATION_KEY] = isEnabled
        }
    }

    override suspend fun saveLockScreenNotification(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOCKSCREEN_NOTIFICATION_KEY] = isEnabled
        }
    }


    override suspend fun isInterruptNotificationEnabled(): Boolean = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[INTERRUPT_NOTIFICATION_KEY] ?: false

    override suspend fun isTimerNotification(): Boolean = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[TIMER_NOTIFICATION_KEY] ?: false

    override suspend fun isLockScreenNotification(): Boolean = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.first()[LOCKSCREEN_NOTIFICATION_KEY] ?: true


    companion object {
        const val NOTIFICATION_PREFERENCES_NAME = "notification_preferences"
        private val INTERRUPT_NOTIFICATION_KEY = booleanPreferencesKey("interrupt_notification")
        private val TIMER_NOTIFICATION_KEY = booleanPreferencesKey("timer_notification")
        private val LOCKSCREEN_NOTIFICATION_KEY = booleanPreferencesKey("lockscreen_notification")

    }
}
