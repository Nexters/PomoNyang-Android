package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroFocusViewModel @Inject constructor() : BaseViewModel<PomodoroFocusState, PomodoroFocusEvent, PomodoroFocusEffect>() {

    override fun setInitialState(): PomodoroFocusState = PomodoroFocusState

    override fun handleEvent(event: PomodoroFocusEvent) {
        when (event) {
            PomodoroFocusEvent.ClickRest -> setEffect(PomodoroFocusEffect.GoToPomodoroRest)
            PomodoroFocusEvent.ClickHome -> setEffect(PomodoroFocusEffect.GoToPomodoroSetting)
        }
    }
}
