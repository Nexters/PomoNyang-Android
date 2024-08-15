package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroTimerViewModel.Companion.DEFAULT_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PomodoroTimerState(
    val type: String = "",
    val focusTime: String = DEFAULT_TIME,
    val exceededTime: String = DEFAULT_TIME,
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
                val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first()
                updateState {
                    copy(
                        type = selectedPomodoroSetting.title,
                        focusTime = (selectedPomodoroSetting.focusTime.times(60)).formatTime(),
                        categoryNo = selectedPomodoroSetting.categoryNo
                    )
                }
            }
        }
    }

    private fun Int.formatTime(): String {
        val minutesPart = this / 60
        val secondsPart = this % 60
        return String.format(Locale.KOREAN, "%02d:%02d", minutesPart, secondsPart)
    }

    companion object {
        const val DEFAULT_TIME = "00:00"
    }
}
