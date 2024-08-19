package com.pomonyang.mohanyang.data.repository.push

import com.pomonyang.mohanyang.data.local.datastore.datasource.notification.NotificationLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.remote.model.request.RegisterPushTokenRequest
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

internal class PushAlarmRepositoryImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource
) : PushAlarmRepository {
    override suspend fun saveFcmToken(fcmToken: String) = tokenLocalDataSource.saveFcmToken(fcmToken)

    override suspend fun getFcmToken(): String = tokenLocalDataSource.getFcmToken()

    override suspend fun registerPushToken(fcmToken: String): Result<Unit> = mohaNyangService.registerPushToken(RegisterPushTokenRequest(deviceToken = fcmToken, deviceType = DEVICE_TYPE))

    override suspend fun unRegisterPushToken(): Result<Unit> = mohaNyangService.unRegisterPushToken()

    override suspend fun subscribeNotification(): Result<Unit> = mohaNyangService.subscribeNotification()

    override suspend fun unSubscribeNotification(): Result<Unit> = mohaNyangService.unSubscribeNotification()
    override suspend fun setInterruptNotification(isEnabled: Boolean) = notificationLocalDataSource.saveInterruptNotification(isEnabled)

    override suspend fun isInterruptNotificationEnabled(): Boolean = notificationLocalDataSource.isInterruptNotificationEnabled()

    override suspend fun setTimerNotification(isEnabled: Boolean) = notificationLocalDataSource.saveTimerNotification(isEnabled)

    override suspend fun isTimerNotificationEnabled(): Boolean = notificationLocalDataSource.isTimerNotification()

    companion object {
        private const val DEVICE_TYPE = "ANDROID"
    }
}
