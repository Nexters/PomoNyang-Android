package com.pomonyang.mohanyang.presentation.screen.home.setting

import androidx.lifecycle.viewModelScope
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetSelectedPomodoroSettingUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import com.pomonyang.mohanyang.presentation.model.category.toCategoryModel
import com.pomonyang.mohanyang.presentation.model.setting.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class PomodoroSettingViewModel @Inject constructor(
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val getSelectedPomodoroSettingUseCase: GetSelectedPomodoroSettingUseCase,
    private val userRepository: UserRepository,
) : BaseViewModel<PomodoroSettingState, PomodoroSettingEvent, PomodoroSettingSideEffect>() {

    override fun setInitialState(): PomodoroSettingState = PomodoroSettingState()

    override fun handleEvent(event: PomodoroSettingEvent) {
        when (event) {
            PomodoroSettingEvent.ClickCategory -> {
                updateState { copy(showCategoryBottomSheet = true) }
            }

            PomodoroSettingEvent.ClickFocusTime -> {
                handleTimeSetting(isFocusTime = true)
            }

            PomodoroSettingEvent.ClickRestTime -> {
                handleTimeSetting(isFocusTime = false)
            }

            PomodoroSettingEvent.ClickMyInfo -> TODO()

            PomodoroSettingEvent.ClickStartPomodoroSetting -> {
                setEffect(PomodoroSettingSideEffect.GoToPomodoro)
            }

            is PomodoroSettingEvent.Init -> {
                collectCategoryData()
                collectSelectedSettingData()
                getMyCatInfo()
            }

            PomodoroSettingEvent.DismissCategoryDialog -> {
                updateState { copy(showCategoryBottomSheet = false) }
            }

            is PomodoroSettingEvent.ClickCategoryConfirmButton -> {
                handleCategoryConfirmButton(event)
                viewModelScope.launch {
                    pomodoroSettingRepository.updateRecentUseCategoryNo(event.confirmCategoryNo)
                }
                updateState { copy(showCategoryBottomSheet = false) }
            }

            PomodoroSettingEvent.DismissOnBoardingTooltip -> {
                updateState { copy(isEndOnBoardingTooltip = true) }
            }

            PomodoroSettingEvent.ClickMenu -> {
                setEffect(PomodoroSettingSideEffect.GoToMyPage)
            }

            is PomodoroSettingEvent.ClickCategoryEdit -> {
                setEffect(PomodoroSettingSideEffect.GoToCategoryEdit(category = event.category))
            }

            PomodoroSettingEvent.ClickCategoryCreate -> {
                setEffect(PomodoroSettingSideEffect.GoToCategoryCreate)
            }
        }
    }

    private fun handleCategoryConfirmButton(event: PomodoroSettingEvent.ClickCategoryConfirmButton) {
        if (state.value.selectedSettingModel.categoryNo != event.confirmCategoryNo) {
            val title = state.value.categoryList.find { it.categoryNo == event.confirmCategoryNo }?.title
            setEffect(PomodoroSettingSideEffect.ShowSnackBar("$title 카테고리로 변경했어요", R.drawable.ic_check))
        }
    }

    private fun handleTimeSetting(isFocusTime: Boolean) {
        val selectedSettingModel = state.value.selectedSettingModel
        val initialTime = if (isFocusTime) selectedSettingModel.focusTime else selectedSettingModel.restTime
        setEffect(
            PomodoroSettingSideEffect.GoTimeSetting(
                isFocusTime = isFocusTime,
                initialTime = initialTime,
                category = selectedSettingModel.title,
            ),
        )
    }

    private fun collectCategoryData() {
        pomodoroSettingRepository.getPomodoroSettingList()
            .onEach { pomodoroSettingList ->
                updateState { copy(categoryList = pomodoroSettingList.map { it.toCategoryModel() }) }
            }
            .launchIn(viewModelScope)
    }

    private fun collectSelectedSettingData() {
        getSelectedPomodoroSettingUseCase()
            .onEach { selectedPomodoroEntity ->
                updateState { copy(selectedSettingModel = selectedPomodoroEntity.toModel()) }
            }
            .launchIn(viewModelScope)
    }

    private fun getMyCatInfo() {
        viewModelScope.launch {
            val myCat = userRepository.getMyInfo().cat.toModel()
            updateState { copy(cat = myCat) }
        }
    }
}
