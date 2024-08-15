package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_FOCUS_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_FOCUS_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

data class PomodoroRestState(
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true
) : ViewState

sealed interface PomodoroRestWaitingEvent : ViewEvent {
    data class Init(
        val exceedTime: Int,
        val focusTime: Int,
        val type: String
    ) : PomodoroRestWaitingEvent

    data object OnNavigationClick : PomodoroRestWaitingEvent
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestWaitingEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestWaitingEvent
}

sealed interface PomodoroRestSideEffect : ViewSideEffect {
    data object GoToPomodoroSetting : PomodoroRestSideEffect
    data class ShowSnackbar(val message: String) : PomodoroRestSideEffect
}

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroRestState, PomodoroRestWaitingEvent, PomodoroRestSideEffect>() {

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestWaitingEvent) {
        Timber.tag("koni").d("handleEvent > $event")
        when (event) {
            is PomodoroRestWaitingEvent.Init -> {
                if (event.exceedTime != 0) {
                    setEffect(PomodoroRestSideEffect.GoToPomodoroSetting)
                }

                viewModelScope.launch {
                    getSelectedPomodoroSettingUseCase().first()?.let {
                        updateState {
                            copy(
                                plusButtonEnabled = it.focusTime >= MAX_FOCUS_TIME,
                                minusButtonEnabled = it.focusTime <= MIN_FOCUS_TIME
                            )
                        }
                    }
                }
            }

            PomodoroRestWaitingEvent.OnNavigationClick -> {
                setEffect(PomodoroRestSideEffect.GoToPomodoroSetting)
            }

            is PomodoroRestWaitingEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(PomodoroRestSideEffect.ShowSnackbar("10분은 집중해야 해요"))
                } else {
                    updateState { copy(plusButtonSelected = event.isPlusButtonSelected, minusButtonSelected = false) }
                }
            }

            is PomodoroRestWaitingEvent.OnMinusButtonClick -> {
                if (state.value.minusButtonEnabled.not()) {
                    setEffect(PomodoroRestSideEffect.ShowSnackbar("최대 60분까지만 집중할 수 있어요"))
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }
        }
    }
}
