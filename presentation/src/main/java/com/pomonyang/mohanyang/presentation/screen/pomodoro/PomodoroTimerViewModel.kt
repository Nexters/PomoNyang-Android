package com.pomonyang.mohanyang.presentation.screen.pomodoro

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.domain.usecase.InsertPomodoroInitialDataUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository,
    private val pomodoroInitialDataUseCase: InsertPomodoroInitialDataUseCase
) : BaseViewModel<PomodoroTimerState, PomodoroTimerEvent, PomodoroTimerEffect>() {

    private val currentFocusTimerId = MutableStateFlow("")

    private val combinedTimerData = currentFocusTimerId.flatMapLatest { timerId ->
        pomodoroTimerRepository.getPomodoroTimer(timerId)
    }

    init {
        loadPomodoroSettingData()
        initPomodoroData()
        loadTimerData()
    }

    private fun loadTimerData() {
        viewModelScope.launch {
            combinedTimerData.collect { timerData ->
                timerData?.let { updateTimerState(it) }
            }
        }
    }

    override fun setInitialState(): PomodoroTimerState = PomodoroTimerState()

    override fun handleEvent(event: PomodoroTimerEvent) {
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            getSelectedPomodoroSettingUseCase().collect { entity ->
                val selectedPomodoroSetting = entity.toModel()
                val cat = userRepository.getMyInfo().cat.toModel()
                updateState {
                    copy(
                        title = selectedPomodoroSetting.title,
                        categoryType = selectedPomodoroSetting.categoryType,
                        cat = cat.type,
                        maxFocusTime = (selectedPomodoroSetting.focusTime.times(60)),
                        maxRestTime = (selectedPomodoroSetting.restTime.times(60))
                    )
                }
            }
        }
    }

    private fun initPomodoroData() {
        viewModelScope.launch {
            val focusTimeKey = UUID.randomUUID().toString()
            currentFocusTimerId.value = focusTimeKey
            pomodoroInitialDataUseCase(focusTimeKey)
        }
    }

    private fun updateTimerState(timerData: PomodoroTimerEntity) {
        val remainingFocusTime = (state.value.maxFocusTime - timerData.focusedTime).coerceAtLeast(0)
        val remainingRestTime = (state.value.maxRestTime - timerData.restedTime).coerceAtLeast(0)
        val focusExceededTime = (timerData.focusedTime - state.value.maxFocusTime).coerceAtLeast(0)
        val restExceededTime = (timerData.restedTime - state.value.maxRestTime).coerceAtLeast(0)

        updateState {
            copy(
                remainingFocusTime = remainingFocusTime,
                focusExceededTime = focusExceededTime,
                remainingRestTime = remainingRestTime,
                restExceededTime = restExceededTime
            )
        }

        if (focusExceededTime == PomodoroConstants.MAX_EXCEEDED_TIME) {
            updateState { copy(forceGoRest = true) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun savePomodoroData() {
        GlobalScope.launch {
            pomodoroTimerRepository.savePomodoroData(currentFocusTimerId.value)
        }
    }

    override fun onCleared() {
        savePomodoroData()
        super.onCleared()
    }
}
