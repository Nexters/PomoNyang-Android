package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.BuildConfig
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
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

    private fun Int.formatTime(): String {
        val minutesPart = this / 60
        val secondsPart = this % 60
        return String.format(Locale.KOREAN, "%02d:%02d", minutesPart, secondsPart)
    }
}

sealed interface PomodoroTimerEvent : ViewEvent {
    data object Init : PomodoroTimerEvent
    data object ClickRest : PomodoroTimerEvent
    data object ClickHome : PomodoroTimerEvent
}

sealed interface PomodoroTimerEffect : ViewSideEffect

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

            PomodoroTimerEvent.ClickRest -> {
            }

            PomodoroTimerEvent.ClickHome -> TODO()
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
                        }
                        copy(exceededTime = newExceedTime)
                    }
                }
            }
        }
    }

    companion object {
        private val TIMER_DELAY = if (BuildConfig.DEBUG) 1_000L else 1_000L
        private const val MAX_EXCEEDED_TIME = 3600
        private const val MIN_FOCUS_TIME = 0
        private const val ONE_SECOND = 1
        const val DEFAULT_TIME = "00:00"
    }
}
