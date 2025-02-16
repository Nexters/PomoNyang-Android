package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.remote.util.InternalException
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@HiltViewModel
class OnboardingNamingCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository,
    private val userRepository: UserRepository,
    private val pomodoroSettingRepository: PomodoroSettingRepository,
) : BaseViewModel<NamingState, NamingEvent, NamingSideEffect>() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is InternalException -> {
                updateState { copy(isInternalError = true) }
            }

            is BadRequestException -> {
                updateState { copy(isInvalidError = true) }
            }

            else -> {
                updateState { copy(isInvalidError = true) }
            }
        }
        updateState { copy(isLoading = false) }
    }

    private val scope = viewModelScope + coroutineExceptionHandler

    override fun setInitialState(): NamingState = NamingState()

    override fun handleEvent(event: NamingEvent) {
        when (event) {
            is NamingEvent.OnComplete -> {
                scope.launch {
                    updateState { copy(isLoading = true, lastRequestAction = event) }
                    pomodoroSettingRepository.fetchPomodoroSettingList()
                    updateCatName(event.name)
                    updateState { copy(isLoading = false) }
                }
            }

            is NamingEvent.OnClickRetry -> {
                state.value.lastRequestAction?.let { handleEvent(it) }
            }
        }
    }

    private suspend fun updateCatName(name: String) {
        catSettingRepository.updateCatInfo(name).mapCatching {
            userRepository.fetchMyInfo().onSuccess {
                setEffect(NamingSideEffect.NavToNext)
            }
        }.getOrThrow()
    }
}
