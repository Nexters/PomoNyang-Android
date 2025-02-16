package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_REST_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_REST_MINUTES
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase,
) : BaseViewModel<PomodoroRestState, PomodoroRestEvent, PomodoroRestEffect>() {

    init {
        viewModelScope.launch {
            val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
            updateState {
                copy(
                    plusButtonEnabled = selectedPomodoroSetting.restTime < MAX_REST_MINUTES,
                    minusButtonEnabled = selectedPomodoroSetting.restTime > MIN_REST_MINUTES,
                )
            }
        }
    }

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestEvent) {
        when (event) {
            is PomodoroRestEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "최대 30분까지만 휴식할 수 있어요",
                            iconRes = R.drawable.ic_clock,
                        ),
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
                            iconRes = R.drawable.ic_clock,
                        ),
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroRestEvent.OnEndPomodoroClick -> {
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToHome)
            }

            PomodoroRestEvent.OnFocusClick -> {
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToPomodoroFocus)
            }
        }
    }

    private fun adjustRestTime() {
        if (state.value.plusButtonSelected || state.value.minusButtonSelected) {
            viewModelScope.launch {
                adjustPomodoroTimeUseCase(
                    isFocusTime = false,
                    isIncrease = state.value.plusButtonSelected,
                )
            }
        }
    }
}
