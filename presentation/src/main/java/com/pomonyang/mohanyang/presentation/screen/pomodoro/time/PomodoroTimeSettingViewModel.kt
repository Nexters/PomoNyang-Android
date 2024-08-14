package com.pomonyang.mohanyang.presentation.screen.pomodoro.time

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroTimeSettingState(
    val categoryNo: Int = 0,
    val titleName: String = "",
    val initialFocusTime: Int = 5,
    val initialRestTime: Int = 5,
    val pickFocusTime: Int = 5,
    val pickRestTime: Int = 5,
    val isFocus: Boolean = false
) : ViewState

sealed interface PomodoroTimeSettingEvent : ViewEvent {
    data class Init(val isFocusTime: Boolean) : PomodoroTimeSettingEvent

    data object Submit : PomodoroTimeSettingEvent

    data class ChangePickTime(
        val time: Int
    ) : PomodoroTimeSettingEvent
}

sealed interface PomodoroTimeSettingEffect : ViewSideEffect {
    data object GoToPomodoroSettingScreen : PomodoroTimeSettingEffect
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
                    getSelectedPomodoroSettingUseCase().first()?.let {
                        updateState {
                            copy(
                                categoryNo = it.categoryNo,
                                titleName = it.title,
                                initialFocusTime = it.focusTime,
                                initialRestTime = it.restTime,
                                isFocus = event.isFocusTime
                            )
                        }
                    } ?: run {
                        // TODO 로컬에 데이터가 없는 경우 처리가 필요할까? 없는 경우가 있을까 고민 중
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
        }
    }

    private fun updatePomodoroCategoryTime() {
        viewModelScope.launch {
            pomodoroSettingRepository.updatePomodoroCategoryTimes(
                categoryNo = state.value.categoryNo,
                titleName = state.value.titleName,
                focusTime = state.value.pickFocusTime,
                restTime = state.value.pickRestTime
            )
        }
    }
}
