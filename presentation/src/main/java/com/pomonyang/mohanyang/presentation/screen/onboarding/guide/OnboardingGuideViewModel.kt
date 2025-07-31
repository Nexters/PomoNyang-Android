package com.pomonyang.mohanyang.presentation.screen.onboarding.guide

import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class OnboardingGuideSideEffect : ViewSideEffect

sealed class OnboardingGuideEvent : ViewEvent {
    data object Init : OnboardingGuideEvent()
}

data class OnboardingGuideUiState(val isNewUser: Boolean) : ViewState

@HiltViewModel
class OnboardingGuideViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<OnboardingGuideUiState, OnboardingGuideEvent, OnboardingGuideSideEffect>() {

    override fun setInitialState(): OnboardingGuideUiState = OnboardingGuideUiState(isNewUser = true)

    override fun handleEvent(event: OnboardingGuideEvent) {}
}
