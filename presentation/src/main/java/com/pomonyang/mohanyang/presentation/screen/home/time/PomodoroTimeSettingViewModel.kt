package com.pomonyang.mohanyang.presentation.screen.home.time

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.remote.util.InternalException
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@HiltViewModel
class PomodoroTimeSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
) : BaseViewModel<PomodoroTimeSettingState, PomodoroTimeSettingEvent, PomodoroTimeSettingEffect>() {

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

    override fun setInitialState(): PomodoroTimeSettingState = PomodoroTimeSettingState()

    override fun handleEvent(event: PomodoroTimeSettingEvent) {
        when (event) {
            is PomodoroTimeSettingEvent.Init -> {
                updateState { copy(lastRequestAction = event) }
                getPomodoroCategoryTimeData(event.isFocusTime)
            }

            is PomodoroTimeSettingEvent.Submit -> {
                updateState { copy(lastRequestAction = event) }
                updatePomodoroCategoryTime()
            }

            is PomodoroTimeSettingEvent.ChangePickTime -> {
                val pickFocusTime = if (state.value.isFocus) event.time else state.value.initialFocusTime
                val pickResetTime = if (state.value.isFocus) state.value.initialRestTime else event.time
                updateState {
                    copy(
                        pickFocusTime = pickFocusTime,
                        pickRestTime = pickResetTime,
                    )
                }
            }

            is PomodoroTimeSettingEvent.ClickClose -> {
                setEffect(PomodoroTimeSettingEffect.ClosePomodoroTimerSettingScreen)
            }

            is PomodoroTimeSettingEvent.ClickRetry -> {
                state.value.lastRequestAction?.let { handleEvent(it) }
            }
        }
    }

    private fun getPomodoroCategoryTimeData(isFocusTime: Boolean) {
        scope.launch {
            updateState { copy(isLoading = true) }
            val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
            updateState {
                copy(
                    categoryNo = selectedPomodoroSetting.categoryNo,
                    titleName = selectedPomodoroSetting.title,
                    initialFocusTime = selectedPomodoroSetting.focusTime,
                    initialRestTime = selectedPomodoroSetting.restTime,
                    isFocus = isFocusTime,
                    isInternalError = false,
                    isInvalidError = false,
                    isLoading = false,
                    categoryIcon = selectedPomodoroSetting.categoryIcon,
                )
            }
        }
    }

    private fun updatePomodoroCategoryTime() {
        scope.launch {
            updateState { copy(isLoading = true) }
            pomodoroSettingRepository.updatePomodoroCategorySetting(
                categoryNo = state.value.categoryNo,
                focusTime = state.value.pickFocusTime,
                restTime = state.value.pickRestTime,
            ).getOrThrow()
            updateState {
                copy(
                    isInternalError = false,
                    isInvalidError = false,
                    isLoading = false,
                )
            }
            setEffect(PomodoroTimeSettingEffect.GoToPomodoroSettingScreen)
        }
    }
}
