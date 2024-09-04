package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_REST_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_REST_MINUTES
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroRestState(
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true
) : ViewState

sealed interface PomodoroRestEvent : ViewEvent {
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestEvent
    data object OnEndPomodoroClick : PomodoroRestEvent
    data object OnFocusClick : PomodoroRestEvent
    data object Init : PomodoroRestEvent
}

sealed interface PomodoroRestEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String, @DrawableRes val iconRes: Int) : PomodoroRestEffect
    data object GoToHome : PomodoroRestEffect
    data object GoToPomodoroFocus : PomodoroRestEffect
    data object SendEndRestAlarm : PomodoroRestEffect
}

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository
) : BaseViewModel<PomodoroRestState, PomodoroRestEvent, PomodoroRestEffect>() {

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestEvent) {
        when (event) {
            PomodoroRestEvent.Init -> viewModelScope.launch {
                val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                updateState {
                    copy(
                        plusButtonEnabled = selectedPomodoroSetting.restTime < MAX_REST_MINUTES,
                        minusButtonEnabled = selectedPomodoroSetting.restTime > MIN_REST_MINUTES
                    )
                }
            }

            is PomodoroRestEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "최대 30분까지만 휴식할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(plusButtonSelected = event.isPlusButtonSelected, minusButtonSelected = false) }
                }
            }

            is PomodoroRestEvent.OnMinusButtonClick -> {
                if (state.value.minusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "5분은 휴식해야 다음에 집중할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroRestEvent.OnEndPomodoroClick -> {
                savePomodoroData()
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToHome)
            }

            PomodoroRestEvent.OnFocusClick -> {
                savePomodoroData()
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToPomodoroFocus)
            }
        }
    }

    private fun savePomodoroData() {
        viewModelScope.launch {
            pomodoroTimerRepository.updatePomodoroDone()
            pomodoroTimerRepository.savePomodoroCacheData()
        }
    }

    private fun adjustRestTime() {
        if (state.value.plusButtonSelected || state.value.minusButtonSelected) {
            viewModelScope.launch {
                adjustPomodoroTimeUseCase(
                    isFocusTime = false,
                    isIncrease = state.value.plusButtonSelected
                )
            }
        }
    }
}
