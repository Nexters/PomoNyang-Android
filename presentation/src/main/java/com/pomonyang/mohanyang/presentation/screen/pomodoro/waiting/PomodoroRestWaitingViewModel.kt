package com.pomonyang.mohanyang.presentation.screen.pomodoro.waiting

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.BuildConfig
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_FOCUS_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_FOCUS_MINUTES
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroRestWaitingState(
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true,
    val forceGoHome: Boolean = false
) : ViewState

sealed interface PomodoroRestWaitingEvent : ViewEvent {
    data class Init(
        val exceedTime: Int,
        val focusTime: Int,
        val type: String
    ) : PomodoroRestWaitingEvent

    data object OnNavigationClick : PomodoroRestWaitingEvent
    data object OnEndFocusClick : PomodoroRestWaitingEvent
    data object OnStartRestClick : PomodoroRestWaitingEvent
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestWaitingEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestWaitingEvent
}

sealed interface PomodoroRestWaitingSideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : PomodoroRestWaitingSideEffect
    data object GoToPomodoroRest : PomodoroRestWaitingSideEffect

    data class ShowSnackbar(
        val message: String,
        @DrawableRes val iconRes: Int
    ) : PomodoroRestWaitingSideEffect
}

@HiltViewModel
class PomodoroRestWaitingViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase
) : BaseViewModel<PomodoroRestWaitingState, PomodoroRestWaitingEvent, PomodoroRestWaitingSideEffect>() {

    override fun setInitialState(): PomodoroRestWaitingState = PomodoroRestWaitingState()

    override fun handleEvent(event: PomodoroRestWaitingEvent) {
        when (event) {
            is PomodoroRestWaitingEvent.Init -> {
                viewModelScope.launch {
                    val pomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                    updateState {
                        copy(
                            plusButtonEnabled = pomodoroSetting.focusTime < MAX_FOCUS_MINUTES,
                            minusButtonEnabled = pomodoroSetting.focusTime > MIN_FOCUS_MINUTES
                        )
                    }
                    startForcePomodoroSettingCountdown()
                }
            }

            PomodoroRestWaitingEvent.OnNavigationClick -> {
                setEffect(PomodoroRestWaitingSideEffect.GoToPomodoroSetting)
            }

            is PomodoroRestWaitingEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestWaitingSideEffect.ShowSnackbar(
                            message = "최대 60분까지만 집중할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(plusButtonSelected = event.isPlusButtonSelected, minusButtonSelected = false) }
                }
            }

            is PomodoroRestWaitingEvent.OnMinusButtonClick -> {
                if (state.value.minusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestWaitingSideEffect.ShowSnackbar(
                            message = "10분은 집중해야 해요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroRestWaitingEvent.OnEndFocusClick -> {
                adjustFocusTime()
                setEffect(PomodoroRestWaitingSideEffect.GoToPomodoroSetting)
            }

            PomodoroRestWaitingEvent.OnStartRestClick -> {
                adjustFocusTime()
                setEffect(PomodoroRestWaitingSideEffect.GoToPomodoroRest)
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
