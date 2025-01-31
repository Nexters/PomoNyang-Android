package com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mohanyang.presentation.BuildConfig
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_FOCUS_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_FOCUS_MINUTES
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroRestWaiting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroBreakReadyViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<PomodoroBreakReadyState, PomodoroBreakReadyEvent, PomodoroBreakReadySideEffect>() {

    init {
        val data = savedStateHandle.toRoute<PomodoroRestWaiting>()
        handleEvent(
            PomodoroBreakReadyEvent.Init(
                exceededTime = data.exceededTime,
                focusTime = data.focusTime,
                type = data.type,
                pomodoroId = data.pomodoroId
            )
        )
    }

    override fun setInitialState(): PomodoroBreakReadyState = PomodoroBreakReadyState()

    override fun handleEvent(event: PomodoroBreakReadyEvent) {
        when (event) {
            is PomodoroBreakReadyEvent.Init -> {
                viewModelScope.launch {
                    val pomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                    updateState {
                        copy(
                            pomodoroId = event.pomodoroId,
                            exceededTime = event.exceededTime,
                            focusedTime = event.focusTime,
                            type = event.type,
                            plusButtonEnabled = pomodoroSetting.focusTime < MAX_FOCUS_MINUTES,
                            minusButtonEnabled = pomodoroSetting.focusTime > MIN_FOCUS_MINUTES
                        )
                    }
                    startForcePomodoroSettingCountdown()
                }
            }

            PomodoroBreakReadyEvent.OnNavigationClick -> {
                setEffect(PomodoroBreakReadySideEffect.GoToPomodoroSetting)
            }

            is PomodoroBreakReadyEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroBreakReadySideEffect.ShowSnackbar(
                            message = "최대 60분까지만 집중할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(plusButtonSelected = event.isPlusButtonSelected, minusButtonSelected = false) }
                }
            }

            is PomodoroBreakReadyEvent.OnMinusButtonClick -> {
                if (state.value.minusButtonEnabled.not()) {
                    setEffect(
                        PomodoroBreakReadySideEffect.ShowSnackbar(
                            message = "10분은 집중해야 해요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroBreakReadyEvent.OnEndFocusClick -> {
                adjustFocusTime()
                setEffect(PomodoroBreakReadySideEffect.GoToPomodoroSetting)
            }

            PomodoroBreakReadyEvent.OnStartRestClick -> {
                adjustFocusTime()
                setEffect(PomodoroBreakReadySideEffect.GoToPomodoroRest)
            }
        }
    }

    private fun startForcePomodoroSettingCountdown() {
        viewModelScope.launch(Dispatchers.IO) {
            var count = 1
            while (true) {
                if (BuildConfig.DEBUG) {
                    delay(1.seconds)
                } else {
                    delay(1.minutes)
                }
                count += 1
                if (count == if (BuildConfig.DEBUG) 10 else 60) {
                    updateState { copy(forceGoHome = true) }
                }
            }
        }
    }

    private fun adjustFocusTime() {
        if (state.value.plusButtonSelected || state.value.minusButtonSelected) {
            viewModelScope.launch {
                adjustPomodoroTimeUseCase(
                    isFocusTime = true,
                    isIncrease = state.value.plusButtonSelected
                )
            }
        }
    }
}
