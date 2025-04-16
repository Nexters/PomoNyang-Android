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
import com.pomonyang.mohanyang.presentation.util.MohanyangEventLog
import com.pomonyang.mohanyang.presentation.util.MohanyangEventLogger
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
    private val mohanyangEventLogger: MohanyangEventLogger,
) : BaseViewModel<PomodoroSettingState, PomodoroSettingEvent, PomodoroSettingSideEffect>() {

    override fun setInitialState(): PomodoroSettingState = PomodoroSettingState()

    override fun handleEvent(event: PomodoroSettingEvent) {
        when (event) {
            PomodoroSettingEvent.ClickCategory -> {
                updateState { copy(showCategoryBottomSheet = true) }
                mohanyangEventLogger.log(MohanyangEventLog.HomeCategoryClick)
            }

            PomodoroSettingEvent.ClickFocusTime -> {
                handleTimeSetting(isFocusTime = true)
            }

            PomodoroSettingEvent.ClickRestTime -> {
                handleTimeSetting(isFocusTime = false)
            }

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

            is PomodoroSettingEvent.SelectCategory -> {
                handleCategoryConfirmButton(event)
                viewModelScope.launch {
                    pomodoroSettingRepository.updateRecentUseCategoryNo(event.categoryNo)
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
                mohanyangEventLogger.log(MohanyangEventLog.CategoryEditClick)
            }

            PomodoroSettingEvent.ClickCategoryCreate -> {
                setEffect(PomodoroSettingSideEffect.GoToCategoryCreate)
                mohanyangEventLogger.log(MohanyangEventLog.CategoryAddClick)
            }

            is PomodoroSettingEvent.DeleteCategories -> {
                viewModelScope.launch {
                    mohanyangEventLogger.log(MohanyangEventLog.CategoryDeleteCTAClick)
                    pomodoroSettingRepository.deleteCategories(event.categoryIds)
                        .onSuccess {
                            mohanyangEventLogger.log(MohanyangEventLog.CategoryDeletedCount(count = event.categoryIds.count()))
                        }
                        .onFailure {
                            setEffect(PomodoroSettingSideEffect.ShowBottomSheetSnackBar(it.message ?: "알 수 없는 오류가 발생했어요.", null))
                        }
                }
            }
        }
    }

    private fun handleCategoryConfirmButton(event: PomodoroSettingEvent.SelectCategory) {
        if (state.value.selectedSettingModel.categoryNo != event.categoryNo) {
            val title = state.value.categoryList.find { it.categoryNo == event.categoryNo }?.title
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
            }.also {
                mohanyangEventLogger.log(MohanyangEventLog.CategoryCreateCount(state.value.categoryList.count()))
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
