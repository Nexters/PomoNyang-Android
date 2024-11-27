package com.pomonyang.mohanyang.data.repository.push

interface PushAlarmRepository {
    suspend fun saveFcmToken(fcmToken: String)
    suspend fun getFcmToken(): String
    suspend fun registerPushToken(fcmToken: String): Result<Unit>
    suspend fun unRegisterPushToken(): Result<Unit>
    suspend fun subscribeNotification(): Result<Unit>
    suspend fun unSubscribeNotification(): Result<Unit>
    suspend fun setInterruptNotification(isEnabled: Boolean)
    suspend fun isInterruptNotificationEnabled(): Boolean
    suspend fun setTimerNotification(isEnabled: Boolean)
    suspend fun isTimerNotificationEnabled(): Boolean
    suspend fun isLockScreenNotificationEnabled(): Boolean
    suspend fun setLockScreenNotification(isEnabled: Boolean)
}
