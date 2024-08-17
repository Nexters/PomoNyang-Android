package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
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
    val selectedType: CatType? = null
) : ViewState

sealed interface SelectCatEvent : ViewEvent {
    data object Init : SelectCatEvent
    data class OnSelectType(val type: CatType) : SelectCatEvent
    data object OnStartClick : SelectCatEvent
}

sealed interface SelectCatSideEffect : ViewSideEffect {
    data class OnNavToNaming(val no: Int, val catName: String?) : SelectCatSideEffect
}

@HiltViewModel
class OnboardingSelectCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository
) : BaseViewModel<SelectCatState, SelectCatEvent, SelectCatSideEffect>() {

    override fun setInitialState(): SelectCatState = SelectCatState()

    override fun handleEvent(event: SelectCatEvent) {
        when (event) {
            is SelectCatEvent.Init -> {
                getCatTypes()
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
        }
    }

    private fun getCatTypes() {
        viewModelScope.launch {
            catSettingRepository.getCatTypes().also { response ->
                response.onSuccess { cats ->
                    updateState { copy(cats = cats.map { it.toModel() }) }
                }
            }
        }
    }

    private fun updateSelectCatType(catNo: Int) {
        viewModelScope.launch {
            catSettingRepository.updateCatType(catNo).onSuccess {
                val catName = state.value.cats.find { it.no == catNo }
                setEffect(SelectCatSideEffect.OnNavToNaming(catNo, catName?.name))
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
