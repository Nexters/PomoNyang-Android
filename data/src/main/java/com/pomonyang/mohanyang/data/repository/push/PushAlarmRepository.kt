package com.pomonyang.mohanyang.data.repository.push

interface PushAlarmRepository {
    suspend fun saveFcmToken(fcmToken: String)
    suspend fun getFcmToken(): String
    suspend fun registerPushToken(fcmToken: String): Result<Unit>
    suspend fun unRegisterPushToken(): Result<Unit>
    suspend fun subscribeNotification(): Result<Unit>
    suspend fun unSubscribeNotification(): Result<Unit>
}
