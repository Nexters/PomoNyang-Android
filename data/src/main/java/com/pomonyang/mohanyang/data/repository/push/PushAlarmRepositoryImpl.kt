package com.pomonyang.mohanyang.data.repository.push

import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.remote.model.request.RegisterPushTokenRequest
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

class PushAlarmRepositoryImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService,
    private val tokenLocalDataSource: TokenLocalDataSource
) : PushAlarmRepository {
    override suspend fun saveFcmToken(fcmToken: String) = tokenLocalDataSource.saveFcmToken(fcmToken)

    override suspend fun getFcmToken(): String = tokenLocalDataSource.getFcmToken()

    override suspend fun registerPushToken(fcmToken: String): Result<Unit> = mohaNyangService.registerPushToken(RegisterPushTokenRequest(fcmToken))

    override suspend fun unRegisterPushToken(): Result<Unit> = mohaNyangService.unRegisterPushToken()

    override suspend fun subscribeNotification(): Result<Unit> = mohaNyangService.subscribeNotification()

    override suspend fun unSubscribeNotification(): Result<Unit> = mohaNyangService.unSubscribeNotification()
}
