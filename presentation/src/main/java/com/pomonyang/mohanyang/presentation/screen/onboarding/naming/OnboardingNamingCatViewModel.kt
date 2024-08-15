package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

object NamingState : ViewState

sealed interface NamingEvent : ViewEvent {
    data class OnComplete(val name: String) : NamingEvent
}

sealed interface NamingSideEffect : ViewSideEffect {
    data object NavToHome : NamingSideEffect
}

@HiltViewModel
class OnboardingNamingCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository,
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : BaseViewModel<NamingState, NamingEvent, NamingSideEffect>() {

    override fun setInitialState(): NamingState = NamingState

    override fun handleEvent(event: NamingEvent) {
        when (event) {
            is NamingEvent.OnComplete -> {
                viewModelScope.launch {
                    pomodoroSettingRepository.fetchPomodoroSettingList()
                }
                updateCatName(event.name)
            }
        }
    }

    private fun updateCatName(name: String) {
        viewModelScope.launch {
            catSettingRepository.updateCatInfo(name).onSuccess {
                setEffect(NamingSideEffect.NavToHome)
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
