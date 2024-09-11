package com.pomonyang.mohanyang.presentation.screen.home.time

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroTimeSettingState(
    val categoryNo: Int = 0,
    val titleName: String = "",
    val initialFocusTime: Int = 10,
    val initialRestTime: Int = 10,
    val pickFocusTime: Int = 10,
    val pickRestTime: Int = 10,
    val isFocus: Boolean = false
) : ViewState

sealed interface PomodoroTimeSettingEvent : ViewEvent {
    data class Init(val isFocusTime: Boolean) : PomodoroTimeSettingEvent

    data object Submit : PomodoroTimeSettingEvent

    data class ChangePickTime(
        val time: Int
    ) : PomodoroTimeSettingEvent

    data object OnCloseClick : PomodoroTimeSettingEvent
}

sealed interface PomodoroTimeSettingEffect : ViewSideEffect {
    data object GoToPomodoroSettingScreen : PomodoroTimeSettingEffect

    data object ClosePomodoroTimerSettingScreen : PomodoroTimeSettingEffect
}

@HiltViewModel
class PomodoroTimeSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroTimeSettingState, PomodoroTimeSettingEvent, PomodoroTimeSettingEffect>() {

    override fun setInitialState(): PomodoroTimeSettingState = PomodoroTimeSettingState()

    override fun handleEvent(event: PomodoroTimeSettingEvent) {
        when (event) {
            is PomodoroTimeSettingEvent.Init -> {
                viewModelScope.launch {
                    val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                    updateState {
                        copy(
                            categoryNo = selectedPomodoroSetting.categoryNo,
                            titleName = selectedPomodoroSetting.title,
                            initialFocusTime = selectedPomodoroSetting.focusTime,
                            initialRestTime = selectedPomodoroSetting.restTime,
                            isFocus = event.isFocusTime
                        )
                    }
                }
            }

            PomodoroTimeSettingEvent.Submit -> {
                updatePomodoroCategoryTime()
                setEffect(PomodoroTimeSettingEffect.GoToPomodoroSettingScreen)
            }

            is PomodoroTimeSettingEvent.ChangePickTime -> {
                val pickFocusTime = if (state.value.isFocus) event.time else state.value.initialFocusTime
                val pickResetTime = if (state.value.isFocus) state.value.initialRestTime else event.time
                updateState {
                    copy(
                        pickFocusTime = pickFocusTime,
                        pickRestTime = pickResetTime
                    )
                }
            }

            is PomodoroTimeSettingEvent.OnCloseClick -> {
                setEffect(PomodoroTimeSettingEffect.ClosePomodoroTimerSettingScreen)
            }
        }
    }

    private fun updatePomodoroCategoryTime() {
        viewModelScope.launch {
            pomodoroSettingRepository.updatePomodoroCategoryTimes(
                categoryNo = state.value.categoryNo,
                focusTime = state.value.pickFocusTime,
                restTime = state.value.pickRestTime
            )
        }
    }
}
