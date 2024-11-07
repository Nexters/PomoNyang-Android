package com.pomonyang.mohanyang.presentation.screen.onboarding.naming

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.remote.util.InternalException
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltViewModel
class OnboardingNamingCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository,
    private val userRepository: UserRepository,
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : BaseViewModel<NamingState, NamingEvent, NamingSideEffect>() {

    override fun setInitialState(): NamingState = NamingState()

    override fun handleEvent(event: NamingEvent) {
        when (event) {
            is NamingEvent.OnComplete -> {
                viewModelScope.launch {
                    updateState { copy(isLoading = true, lastRequestAction = event) }
                    try {
                        pomodoroSettingRepository.fetchPomodoroSettingList()
                        updateCatName(event.name)

                    } catch (e: InternalException) {
                        updateState { copy(isInternalError = true) }
                    } catch (e: BadRequestException) {
                        updateState { copy(isInvalidError = true) }
                    } catch (e: Exception) {
                        Timber.d(e);
                    } finally {
                        updateState { copy(isLoading = false) }
                    }

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
