package com.pomonyang.mohanyang.presentation.screen.pomodoro.focus

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
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroFocusViewModel @Inject constructor(
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val userRepository: UserRepository,
    private val pomodoroInitialDataUseCase: InsertPomodoroInitialDataUseCase,
) : BaseViewModel<PomodoroFocusState, PomodoroFocusEvent, PomodoroFocusEffect>() {

    init {
        handleEvent(PomodoroFocusEvent.Init)
    }

    override fun setInitialState(): PomodoroFocusState = PomodoroFocusState()

    override fun handleEvent(event: PomodoroFocusEvent) {
        when (event) {
            PomodoroFocusEvent.ClickRest -> setEffect(PomodoroFocusEffect.GoToPomodoroRest)
            PomodoroFocusEvent.ClickHome -> {
                setEffect(PomodoroFocusEffect.GoToPomodoroSetting)
                savePomodoroData()
            }

            is PomodoroFocusEvent.Init -> {
                loadPomodoroSettingData()
                loadPomodoroTimerData()
                initPomodoroData()
            }
        }
    }

    private fun loadPomodoroTimerData() {
        pomodoroTimerRepository.getPomodoroTimer(state.value.pomodoroId)
            .onEach { timerData ->
                timerData?.let { updateTimerState(it) }
            }
            .launchIn(viewModelScope)
    }

    private fun initPomodoroData() {
        viewModelScope.launch {
            pomodoroInitialDataUseCase(state.value.pomodoroId)
        }
    }

    private fun loadPomodoroSettingData() {
        viewModelScope.launch {
            getSelectedPomodoroSettingUseCase().collect { entity ->
                val selectedPomodoroSetting = entity.toModel()
                val cat = userRepository.getMyInfo().cat.toModel()
                updateState {
                    copy(
                        pomodoroId = pomodoroId,
                        title = selectedPomodoroSetting.title,
                        categoryIcon = selectedPomodoroSetting.categoryType,
                        cat = cat.type,
                        maxFocusTime = (selectedPomodoroSetting.focusTime.times(60)),
                    )
                }
            }
        }
    }

    private fun updateTimerState(timerData: PomodoroTimerEntity) {
        val remainingFocusTime = (state.value.maxFocusTime - timerData.focusedTime).coerceAtLeast(0)
        val focusExceededTime = (timerData.focusedTime - state.value.maxFocusTime).coerceAtLeast(0)

        updateState {
            copy(
                remainingFocusTime = remainingFocusTime,
                focusExceededTime = focusExceededTime,
            )
        }

        if (focusExceededTime == PomodoroConstants.MAX_EXCEEDED_TIME) {
            updateState { copy(forceGoRest = true) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun savePomodoroData() {
        GlobalScope.launch {
            pomodoroTimerRepository.savePomodoroData(state.value.pomodoroId)
        }
    }
}
