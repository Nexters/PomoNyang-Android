package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data object PomodoroFocusState : ViewState

sealed interface PomodoroFocusEvent : ViewEvent {
    data object ClickRest : PomodoroFocusEvent
    data object ClickHome : PomodoroFocusEvent
    data object Resume : PomodoroFocusEvent
    data object Pause : PomodoroFocusEvent
    data object Dispose : PomodoroFocusEvent
}

sealed interface PomodoroFocusEffect : ViewSideEffect {
    data object GoToPomodoroRest : PomodoroFocusEffect
    data object GoToPomodoroSetting : PomodoroFocusEffect
    data object StartFocusAlarm : PomodoroFocusEffect
    data object StopFocusAlarm : PomodoroFocusEffect
}

@HiltViewModel
class PomodoroFocusViewModel @Inject constructor(
    private val pomodoroTimerRepository: PomodoroTimerRepository
) : BaseViewModel<PomodoroFocusState, PomodoroFocusEvent, PomodoroFocusEffect>() {

    override fun setInitialState(): PomodoroFocusState = PomodoroFocusState

    override fun handleEvent(event: PomodoroFocusEvent) {
        when (event) {
            PomodoroFocusEvent.ClickRest -> setEffect(PomodoroFocusEffect.GoToPomodoroRest)

            PomodoroFocusEvent.ClickHome -> {
                viewModelScope.launch {
                    pomodoroTimerRepository.updatePomodoroDone()
                    pomodoroTimerRepository.savePomodoroCacheData()
                }
                setEffect(PomodoroFocusEffect.GoToPomodoroSetting)
            }

            PomodoroFocusEvent.Pause -> setEffect(PomodoroFocusEffect.StartFocusAlarm)
            PomodoroFocusEvent.Resume -> setEffect(PomodoroFocusEffect.StopFocusAlarm)
            PomodoroFocusEvent.Dispose -> setEffect(PomodoroFocusEffect.StopFocusAlarm)
        }
    }
}
