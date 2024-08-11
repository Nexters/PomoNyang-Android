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
    data class OnChangedName(val name: String) : NamingEvent
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
            is NamingEvent.OnChangedName -> {
                if (event.name.isEmpty()) return
                validateName(event.name)
            }

            is NamingEvent.OnComplete -> {
                setEffect(NamingSideEffect.NavToHome)
            }
        }
    }

    private fun validateName(name: String) {
        val verifyResult = catNameVerifier.verifyName(name)
        if (!state.value.isError && verifyResult.isValid) return
        updateState { copy(isError = !verifyResult.isValid, errorMessage = verifyResult.message) }
    }

    companion object {
        private val catNameVerifier = CatNameVerifier()
    }
}
