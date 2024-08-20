package com.pomonyang.mohanyang.data.local.datastore.datasource.notification

interface NotificationLocalDataSource {
    suspend fun saveInterruptNotification(isEnabled: Boolean)
    suspend fun saveTimerNotification(isEnabled: Boolean)
    suspend fun isInterruptNotificationEnabled(): Boolean
    suspend fun isTimerNotification(): Boolean
}
