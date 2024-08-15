package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.ONE_SECOND
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.TIMER_DELAY
import com.pomonyang.mohanyang.presentation.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class PomodoroRestState(
    val maxRestTime: Int = 0,
    val currentRestTime: Int = 0,
    val exceededTime: Int = 0,
    val type: String = "",
    val categoryNo: Int = -1
) : ViewState {
    fun displayRestTime(): String = currentRestTime.formatTime()
    fun displayExceedTime(): String = exceededTime.formatTime()
}

sealed interface PomodoroRestEvent : ViewEvent {
    data object Init : PomodoroRestEvent
}

sealed interface PomodoroRestEffect : ViewSideEffect

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroRestState, PomodoroRestEvent, PomodoroRestEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestEvent) {
        when (event) {
            PomodoroRestEvent.Init -> viewModelScope.launch {
                val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first()
                updateState {
                    copy(
                        type = selectedPomodoroSetting.title,
                        maxRestTime = (selectedPomodoroSetting.focusTime.times(60)),
                        categoryNo = selectedPomodoroSetting.categoryNo
                    )
                }
                startTimer()
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(TIMER_DELAY)
                updateState {
                    if (currentRestTime < maxRestTime) {
                        copy(currentRestTime = currentRestTime + ONE_SECOND)
                    } else {
                        val newExceedTime = exceededTime + ONE_SECOND
                        if (newExceedTime >= MAX_EXCEEDED_TIME) {
                            timerJob?.cancel()
                        }
                        copy(exceededTime = newExceedTime)
                    }
                }
            }
        }
    }
}
