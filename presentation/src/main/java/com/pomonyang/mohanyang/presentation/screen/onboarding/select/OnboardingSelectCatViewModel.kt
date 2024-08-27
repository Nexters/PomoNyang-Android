package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

data class SelectCatState(
    val cats: List<CatInfoModel> = emptyList(),
    val selectedType: CatType? = null,
    val isLoading: Boolean = true
) : ViewState

sealed interface SelectCatEvent : ViewEvent {
    data class Init(val catNo: Int? = null) : SelectCatEvent
    data class OnSelectType(val type: CatType) : SelectCatEvent
    data object OnStartClick : SelectCatEvent
    data object OnGrantedAlarmPermission : SelectCatEvent
}

sealed interface SelectCatSideEffect : ViewSideEffect {
    data class OnNavToNaming(val no: Int, val catName: String, val catTypeName: String) : SelectCatSideEffect
    data object GoToBack : SelectCatSideEffect
    data class ShowSnackBar(val message: String) : SelectCatSideEffect
}

@HiltViewModel
class OnboardingSelectCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository,
    private val pushAlarmRepository: PushAlarmRepository,
    private val userRepository: UserRepository
) : BaseViewModel<SelectCatState, SelectCatEvent, SelectCatSideEffect>() {

    override fun setInitialState(): SelectCatState = SelectCatState()

    override fun handleEvent(event: SelectCatEvent) {
        when (event) {
            is SelectCatEvent.Init -> {
                getCatTypes(event.catNo)
            }

            is SelectCatEvent.OnSelectType -> {
                updateState { copy(selectedType = event.type) }
            }

            is SelectCatEvent.OnStartClick -> {
                with(state.value) {
                    cats.find { it.type == selectedType }?.no
                }?.let { selectedNo ->
                    updateSelectCatType(selectedNo)
                }
            }

            SelectCatEvent.OnGrantedAlarmPermission -> {
                viewModelScope.launch {
                    pushAlarmRepository.setInterruptNotification(true)
                    pushAlarmRepository.setTimerNotification(true)
                }
            }
        }
    }

    private fun getCatTypes(selectedCatNo: Int?) {
        viewModelScope.launch {
            catSettingRepository.getCatTypes().also { response ->
                response.onSuccess { cats ->
                    val catList = cats.map { it.toModel() }
                    updateState {
                        copy(
                            cats = catList,
                            selectedType = catList.find { it.no == selectedCatNo }?.type,
                            isLoading = false
                        )
                    }
                }.onFailure {
                    Timber.e(it)
                    setEffect(SelectCatSideEffect.GoToBack, SelectCatSideEffect.ShowSnackBar("고양이 선택을 위해 네트워크 연결이 필요해요"))
                }
            }
        }
    }

    private fun updateSelectCatType(catNo: Int) {
        viewModelScope.launch {
            catSettingRepository.updateCatType(catNo).onSuccess {
                userRepository.fetchMyInfo().onSuccess {
                    val cat = state.value.cats.find { it.no == catNo }
                    setEffect(SelectCatSideEffect.OnNavToNaming(catNo, cat?.name ?: "", state.value.selectedType?.name ?: ""))
                }
            }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
