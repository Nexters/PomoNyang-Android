package com.pomonyang.mohanyang.presentation.screen.onboarding.guide

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

sealed class OnboardingGuideSideEffect : ViewSideEffect {
    data object NavigateToHome : OnboardingGuideSideEffect()
}

sealed class OnboardingGuideEvent : ViewEvent {
    data object Init : OnboardingGuideEvent()
}

data class OnboardingGuideUiState(val isNewUser: Boolean) : ViewState

@HiltViewModel
class OnboardingGuideViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<OnboardingGuideUiState, OnboardingGuideEvent, OnboardingGuideSideEffect>() {

    override fun setInitialState(): OnboardingGuideUiState = OnboardingGuideUiState(isNewUser = true)

    override fun handleEvent(event: OnboardingGuideEvent) {
        when (event) {
            is OnboardingGuideEvent.Init -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            if (userRepository.isNewUser()) {
                getTokenByDeviceId()
            }

            setEffect(OnboardingGuideSideEffect.NavigateToHome)
        }
    }

    private suspend fun getTokenByDeviceId() {
        val deviceId = userRepository.getDeviceId()
        if (deviceId.isNotEmpty()) {
            userRepository.login(deviceId).onSuccess {
                userRepository.saveToken(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                )
            }.onFailure {
                Timber.e("token fail: $it")
            }
        }
    }
}
