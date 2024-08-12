package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.domain.model.cat.CatSelectionContent
import com.pomonyang.mohanyang.domain.model.cat.CatType
import com.pomonyang.mohanyang.domain.model.cat.toModel
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class SelectCatState(
    val cats: List<CatSelectionContent> = emptyList(),
    val selectedType: CatType? = null
) : ViewState

sealed interface SelectCatEvent : ViewEvent {
    data object Init : SelectCatEvent
    data class OnSelectType(val type: CatType) : SelectCatEvent
    data object OnStartClick : SelectCatEvent
}

sealed interface SelectCatSideEffect : ViewSideEffect {
    data class OnNavToNaming(val no: Int) : SelectCatSideEffect
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
                    setEffect(SelectCatSideEffect.OnNavToNaming(selectedNo))
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
}
