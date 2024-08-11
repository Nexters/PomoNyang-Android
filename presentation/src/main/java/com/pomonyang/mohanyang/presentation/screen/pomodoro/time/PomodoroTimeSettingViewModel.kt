package com.pomonyang.mohanyang.presentation.screen.pomodoro.time

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class PomodoroTimeSettingState(
    val categoryNo: Int = 0,
    val titleName: String = "",
    val focusTime: Int = 0,
    val restTime: Int = 0,
    val isFocus: Boolean = false
) : ViewState

sealed interface PomodoroTimeSettingEvent : ViewEvent {
    data class Init(
        val categoryNo: Int,
        val titleName: String,
        val focusTime: Int,
        val restTime: Int,
        val isFocusTime: Boolean
    ) : PomodoroTimeSettingEvent

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
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : BaseViewModel<PomodoroTimeSettingState, PomodoroTimeSettingEvent, PomodoroTimeSettingEffect>() {

    override fun setInitialState(): PomodoroTimeSettingState = PomodoroTimeSettingState()

    override fun handleEvent(event: PomodoroTimeSettingEvent) {
        when (event) {
            is PomodoroTimeSettingEvent.Init -> {
                updateState {
                    copy(
                        categoryNo = event.categoryNo,
                        titleName = event.titleName,
                        focusTime = event.focusTime,
                        restTime = event.restTime,
                        isFocus = event.isFocusTime
                    )
                }
            }

            PomodoroTimeSettingEvent.Submit -> {
                updatePomodoroCategoryTime()
                setEffect(PomodoroTimeSettingEffect.GoToPomodoroSettingScreen)
            }

            is PomodoroTimeSettingEvent.ChangePickTime -> {
                val pickFocusTime = if (state.value.isFocus) event.time else state.value.focusTime
                val pickResetTime = if (state.value.isFocus) state.value.restTime else event.time
                updateState {
                    copy(
                        focusTime = pickFocusTime,
                        restTime = pickResetTime
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
                focusTime = state.value.focusTime,
                restTime = state.value.restTime
            )
        }
    }
}
