package com.pomonyang.mohanyang.presentation.screen.onboarding

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class OnboardingSideEffect : ViewSideEffect {
    data object NavigateToHome : OnboardingSideEffect()
}

sealed class OnboardingEvent : ViewEvent {
    data object Init : OnboardingEvent()
}

data class OnboardUiState(val isNewUser: Boolean) : ViewState

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<OnboardUiState, OnboardingEvent, OnboardingSideEffect>() {

    private fun login() {
        viewModelScope.launch {
            if (userRepository.isNewUser()) {
            }
            getTokenByDeviceId()

            setEffect(OnboardingSideEffect.NavigateToHome)
        }
    }

    private suspend fun getTokenByDeviceId() {
        val deviceId = userRepository.getDeviceId()
        if (deviceId.isNotEmpty()) {
            userRepository.login(deviceId).onSuccess {
                userRepository.saveToken(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            }.onFailure {
                Timber.e("token fail: $it")
            }
        }
    }

    override fun setInitialState(): OnboardUiState = OnboardUiState(isNewUser = true)

    override suspend fun handleEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.Init -> {
                login()
            }
        }
    }
}
