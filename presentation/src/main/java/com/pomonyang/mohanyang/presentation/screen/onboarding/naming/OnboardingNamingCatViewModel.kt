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
class OnboardingNamingCatViewModel @Inject constructor() :
    BaseViewModel<
        NamingState,
        NamingEvent,
        NamingSideEffect
        >() {

    override fun setInitialState(): NamingState = NamingState()

    override fun handleEvent(event: NamingEvent) {
        when (event) {
            is NamingEvent.OnComplete -> {
                val verifyResult = verifyName(event.name)
                val isVerifiedName = verifyResult.first
                val errorMessage = verifyResult.second

                if (isVerifiedName) {
                    setEffect(NamingSideEffect.NavToHome)
                } else {
                    updateState {
                        state.value.copy(
                            isError = true,
                            errorMessage = errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun verifyName(name: String): Pair<Boolean, String> {
        /* 공백 포함 최대 8자리 허용 */
        if (name.length > NAME_MAX_LENGTH) return false to "최대 8자리"

        /* 특수문자 비허용 */

        val namePattern = "^[\\w\\s0-9]{${NAME_MIN_LENGTH},${NAME_MAX_LENGTH}}$"
        if (namePattern.toRegex().matches(name)) {
            return true to ""
        }

        return false to "특수 문자 불가능"
    }

    companion object {
        const val NAME_MAX_LENGTH = 8
        const val NAME_MIN_LENGTH = 1
    }
}
