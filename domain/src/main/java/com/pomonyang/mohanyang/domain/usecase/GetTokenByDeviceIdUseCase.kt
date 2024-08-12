package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.repository.user.UserRepository
import javax.inject.Inject

class GetTokenByDeviceIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        val deviceId = userRepository.getDeviceId()
        if (deviceId.isNotEmpty()) {
            userRepository.login(deviceId).onSuccess {
                userRepository.saveToken(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            }.onFailure { throw it }
        }
    }
}
