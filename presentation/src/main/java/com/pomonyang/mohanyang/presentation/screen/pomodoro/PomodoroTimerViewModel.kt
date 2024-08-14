package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroTimerState(
    val type: String = "",
    val focusTime: Int = 0,
    val exceededTime: Int = 0,
    val categoryNo: Int = -1
) : ViewState

sealed interface PomodoroTimerEvent : ViewEvent {
    data object Init : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {
    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
        when (event) {
            PomodoroTimerEvent.Init -> viewModelScope.launch {
                val pomodoroSettingData = getSelectedPomodoroSettingUseCase().first()
                updateState {
                    copy(
                        type = pomodoroSettingData?.title ?: "",
                        focusTime = pomodoroSettingData?.focusTime?.toInt() ?: 0,
                        categoryNo = pomodoroSettingData?.categoryNo ?: -1
                    )
                }
            }
        }
    }
}
