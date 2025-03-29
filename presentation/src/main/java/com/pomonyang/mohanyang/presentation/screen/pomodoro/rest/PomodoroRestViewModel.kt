package com.pomonyang.mohanyang.presentation.screen.pomodoro.rest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.AdjustPomodoroTimeUseCase
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MAX_REST_MINUTES
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.MIN_REST_MINUTES
import com.pomonyang.mohanyang.presentation.screen.pomodoro.PomodoroRest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroRestViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val adjustPomodoroTimeUseCase: AdjustPomodoroTimeUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PomodoroRestState, PomodoroRestEvent, PomodoroRestEffect>() {

    init {
        val pomodoroId = savedStateHandle.toRoute<PomodoroRest>().pomodoroId
        handleEvent(PomodoroRestEvent.Init(pomodoroId))
    }

    override fun setInitialState(): PomodoroRestState = PomodoroRestState()

    override fun handleEvent(event: PomodoroRestEvent) {
        when (event) {
            is PomodoroRestEvent.OnPlusButtonClick -> {
                if (state.value.plusButtonEnabled.not()) {
                    setEffect(
                        PomodoroRestEffect.ShowSnackbar(
                            message = "최대 30분까지만 휴식할 수 있어요",
                            iconRes = R.drawable.ic_clock,
                        ),
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
                            iconRes = R.drawable.ic_clock,
                        ),
                    )
                } else {
                    updateState { copy(minusButtonSelected = event.isMinusButtonSelected, plusButtonSelected = false) }
                }
            }

            PomodoroRestEvent.OnEndPomodoroClick -> {
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToHome)
            }

            PomodoroRestEvent.OnFocusClick -> {
                adjustRestTime()
                setEffect(PomodoroRestEffect.GoToPomodoroFocus)
            }

            is PomodoroRestEvent.Init -> {
                updateState { copy(pomodoroId = event.pomodoroId) }
                loadPomodoroTimerData(event.pomodoroId)
                loadPomodoroSettingData()
            }
        }
    }

    private fun adjustRestTime() {
        if (state.value.plusButtonSelected || state.value.minusButtonSelected) {
            viewModelScope.launch {
                adjustPomodoroTimeUseCase(
                    isFocusTime = false,
                    isIncrease = state.value.plusButtonSelected,
                )
            }
        }
    }

    private fun loadPomodoroTimerData(pomodoroId: String) {
        pomodoroTimerRepository.getPomodoroTimer(
            pomodoroId,
        ).onEach { timerData ->
            timerData?.let { updateTimerState(it) }
        }.launchIn(viewModelScope)
    }

    private fun updateTimerState(timerData: PomodoroTimerEntity) {
        val remainingRestTime = (state.value.maxRestTime - timerData.restedTime).coerceAtLeast(0)
        val restExceededTime = (timerData.restedTime - state.value.maxRestTime).coerceAtLeast(0)

        updateState {
            copy(
                remainingRestTime = remainingRestTime,
                restExceededTime = restExceededTime,
            )
        }
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            val selectedPomodoroSetting = getSelectedPomodoroSettingUseCase().first().toModel()
            val cat = userRepository.getMyInfo().cat.toModel()
            updateState {
                copy(
                    plusButtonEnabled = selectedPomodoroSetting.restTime < MAX_REST_MINUTES,
                    minusButtonEnabled = selectedPomodoroSetting.restTime > MIN_REST_MINUTES,
                    maxRestTime = (selectedPomodoroSetting.restTime.times(60)),
                    categoryIcon = selectedPomodoroSetting.categoryIcon,
                    categoryName = selectedPomodoroSetting.title,
                    cat = cat.type,
                )
            }
        }
    }

    override fun onCleared() {
        savePomodoroData()
        super.onCleared()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun savePomodoroData() {
        GlobalScope.launch {
            pomodoroTimerRepository.savePomodoroData(state.value.pomodoroId)
        }
    }
}
