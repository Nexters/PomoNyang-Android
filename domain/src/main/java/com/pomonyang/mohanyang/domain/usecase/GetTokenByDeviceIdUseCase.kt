package com.pomonyang.mohanyang.domain.usecase

import com.pomonyang.mohanyang.data.remote.model.response.TokenResponse
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import javax.inject.Inject

class GetTokenByDeviceIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<TokenResponse> {
        val deviceId = userRepository.getDeviceId()
        return userRepository.login(deviceId).onSuccess {
            userRepository.saveToken(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
        }
    }
}
