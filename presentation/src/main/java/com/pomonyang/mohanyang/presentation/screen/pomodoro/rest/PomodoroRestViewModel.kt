package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.annotation.DrawableRes
import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_EXCEEDED_TIME
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_REST_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_REST_MINUTES
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
    val categoryType: String = "",
    val categoryNo: Int = -1,
    val catType: CatType = CatType.CHEESE,
    val plusButtonSelected: Boolean = false,
    val minusButtonSelected: Boolean = false,
    val plusButtonEnabled: Boolean = true,
    val minusButtonEnabled: Boolean = true
) : ViewState {
    fun displayRestTime(): String = currentRestTime.formatTime()
    fun displayExceedTime(): String = exceededTime.formatTime()
}

sealed interface PomodoroRestEvent : ViewEvent {
    data class OnPlusButtonClick(val isPlusButtonSelected: Boolean) : PomodoroRestEvent
    data class OnMinusButtonClick(val isMinusButtonSelected: Boolean) : PomodoroRestEvent
    data object OnEndPomodoroClick : PomodoroRestEvent
    data object OnFocusClick : PomodoroRestEvent
    data object Init : PomodoroRestEvent
}

sealed interface PomodoroRestEffect : ViewSideEffect {
    data class ShowSnackbar(val message: String, @DrawableRes val iconRes: Int) : PomodoroRestEffect
    data object GoToHome : PomodoroRestEffect
    data object GoToPomodoroFocus : PomodoroRestEffect
    data object SendEndRestAlarm : PomodoroRestEffect
}

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository
) : BaseViewModel<PomodoroRestState, PomodoroRestEvent, PomodoroRestEffect>() {

    private var timerJob: Job? = null

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestEvent) {
        when (event) {
            PomodoroRestEvent.Init -> viewModelScope.launch {
                val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
                val cat = userRepository.getMyInfo().cat.toModel()
                updateState {
                    copy(
                        categoryType = selectedPomodoroSetting.title,
                        maxRestTime = (selectedPomodoroSetting.focusTime.times(60)),
                        categoryNo = selectedPomodoroSetting.categoryNo,
                        catType = cat.type,
                        plusButtonEnabled = selectedPomodoroSetting.restTime < MAX_REST_MINUTES,
                        minusButtonEnabled = selectedPomodoroSetting.restTime > MIN_REST_MINUTES
                    )
                }
                startTimer()
            }

            is PomodoroRestEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "최대 30분까지만 휴식할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(plusButtonSelected = event.isPlusButtonSelected, minusButtonSelected = false) }
                }
            }

            is PomodoroRestEvent.OnMinusButtonClick -> {
                if (state.value.minusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "5분은 휴식해야 다음에 집중할 수 있어요",
                            iconRes = R.drawable.ic_clock
                        )
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroRestEvent.OnEndPomodoroClick -> {
                savePomodoroData()
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToHome)
            }

            PomodoroRestEvent.OnFocusClick -> {
                savePomodoroData()
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToPomodoroFocus)
            }
        }
    }

    private fun savePomodoroData() {
        viewModelScope.launch {
            pomodoroTimerRepository.updatePomodoroDone()
            pomodoroTimerRepository.savePomodoroCacheData()
        }
    }

    private fun adjustRestTime() {
        if (state.value.plusButtonSelected || state.value.minusButtonSelected) {
            viewModelScope.launch {
                adjustPomodoroTimeUseCase(
                    isFocusTime = false,
                    isIncrease = state.value.plusButtonSelected
                )
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
                        val newRestTime = currentRestTime + ONE_SECOND
                        viewModelScope.launch {
                            pomodoroTimerRepository.updateRestedTime(newRestTime)
                        }
                        copy(currentRestTime = newRestTime)
                    } else {
                        if (exceededTime == 0) setEffect(PomodoroRestEffect.SendEndRestAlarm)
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
