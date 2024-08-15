package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.BuildConfig
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
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
import timber.log.Timber

data class PomodoroTimerState(
    val focusTime: Int = 0,
    val exceededTime: Int = 0,
    val type: String = "",
    val categoryNo: Int = -1
) : ViewState {
    fun displayFocusTime(): String = focusTime.formatTime()
    fun displayRestTime(): String = exceededTime.formatTime()
}

sealed interface PomodoroTimerEvent : ViewEvent {
    data object Init : PomodoroTimerEvent
    data object ClickRest : PomodoroTimerEvent
    data object ClickHome : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect {
    data object GoToPomodoroRest : PomodoroTimerEffect
    data object GoToPomodoroSetting : PomodoroTimerEffect
}

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
        Timber.tag("koni").d("handleEvent > $event")
        when (event) {
            PomodoroTimerEvent.Init -> viewModelScope.launch {
                getSelectedPomodoroSettingUseCase().first()?.let {
                    updateState {
                        copy(
                            type = it.title,
                            focusTime = (it.focusTime.times(60)).formatTime(),
                            categoryNo = it.categoryNo
                        )
                    }
                } ?: run { /* TODO 예외 처리 필요할지 고민 */ }
            }

            PomodoroTimerEvent.ClickRest -> setEffect(PomodoroTimerEffect.GoToPomodoroRest)
            PomodoroTimerEvent.ClickHome -> setEffect(PomodoroTimerEffect.GoToPomodoroSetting)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(TIMER_DELAY)
                updateState {
                    if (focusTime > MIN_FOCUS_TIME) {
                        copy(focusTime = focusTime - ONE_SECOND)
                    } else {
                        val newExceedTime = exceededTime + ONE_SECOND
                        if (newExceedTime >= MAX_EXCEEDED_TIME) {
                            timerJob?.cancel()
                            setEffect(PomodoroTimerEffect.GoToPomodoroRest)
                        }
                        copy(exceededTime = newExceedTime)
                    }
                }
            }
        }
    }

    companion object {
        private val TIMER_DELAY = if (BuildConfig.DEBUG) 10L else 1_000L
        private val MAX_EXCEEDED_TIME = if (BuildConfig.DEBUG) 600 else 3600
        private const val MIN_FOCUS_TIME = 0
        private const val ONE_SECOND = 1
        const val DEFAULT_TIME = "00:00"
    }
}
