package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class NamingState(
    val isError: Boolean = false,
    val errorMessage: String = ""
) : ViewState

sealed interface NamingEvent : ViewEvent {
    data class OnComplete(val name: String) : NamingEvent
}

sealed interface NamingSideEffect : ViewSideEffect {
    data object NavToHome : NamingSideEffect
}

@HiltViewModel
class OnboardingNamingCatViewModel @Inject constructor() : BaseViewModel<NamingState, NamingEvent, NamingSideEffect>() {

    override fun setInitialState(): NamingState = NamingState()

    override fun handleEvent(event: NamingEvent) {
        when (event) {
            is NamingEvent.OnComplete -> {
                val verifyResult = catNameVerifier.verifyName(name = event.name)

                if (verifyResult.isValid) {
                    setEffect(NamingSideEffect.NavToHome)
                } else {
                    updateState {
                        state.value.copy(
                            isError = true,
                            errorMessage = verifyResult.message
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val catNameVerifier = CatNameVerifier()
    }
}
