package com.pomonyang.mohanyang.data.repository.user

import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.token.TokenLocalDataSource
import com.pomonyang.mohanyang.data.local.datastore.datasource.user.UserLocalDataSource
import com.pomonyang.mohanyang.data.remote.model.request.TokenRequest
import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse
import com.pomonyang.mohanyang.data.remote.service.AuthService
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

internal class UserRepositoryImpl @Inject constructor(
    private val deviceLocalDataStore: DeviceIdLocalDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mohaNyangService: MohaNyangService,
    private val authService: AuthService,
) : UserRepository {
    override suspend fun getDeviceId() = deviceLocalDataStore.getDeviceId()

    override fun isNewUser(): Boolean = runBlocking { tokenLocalDataSource.getAccessToken().isEmpty() || userLocalDataSource.getUserInfo().isNewUser() }

    override suspend fun login(deviceId: String) = authService.getTokenByDeviceId(TokenRequest(deviceId))

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        tokenLocalDataSource.saveAccessToken(accessToken)
        tokenLocalDataSource.saveRefreshToken(refreshToken)
    }

    override suspend fun fetchMyInfo(): Result<UserInfoResponse> = mohaNyangService.getMyInfo().onSuccess { userLocalDataSource.saveUserInfo(it) }

    override suspend fun getMyInfo() = userLocalDataSource.getUserInfo()

    override suspend fun getJoinedDate(): LocalDate {
        val createdAtString = userLocalDataSource.getUserInfo().createdAt
        return try {
            // 먼저 ISO_LOCAL_DATE_TIME 형식으로 파싱 시도
            LocalDateTime.parse(createdAtString).toLocalDate()
        } catch (e: Exception) {
            try {
                // 타임존 정보가 포함된 경우 ZonedDateTime으로 파싱 후 LocalDateTime으로 변환
                ZonedDateTime.parse(createdAtString).toLocalDateTime().toLocalDate()
            } catch (e2: Exception) {
                // 마지막으로 ISO_INSTANT 형식 시도 (UTC 기준)
                LocalDateTime.parse(createdAtString, java.time.format.DateTimeFormatter.ISO_INSTANT).toLocalDate()
            }
        }
    }
}
