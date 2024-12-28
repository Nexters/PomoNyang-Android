package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType

data class SelectCatState(
    override val isLoading: Boolean = true,
    override val isInternalError: Boolean = false,
    override val isInvalidError: Boolean = false,
    override val lastRequestAction: SelectCatEvent? = null,
    val cats: List<CatInfoModel> = emptyList(),
    val selectedType: CatType? = null
) : NetworkViewState()

sealed interface SelectCatEvent : ViewEvent {
    data class Init(val catNo: Int? = null) : SelectCatEvent
    data class OnSelectType(val type: CatType) : SelectCatEvent
    data object OnStartClick : SelectCatEvent
    data object OnGrantedAlarmPermission : SelectCatEvent
    data object OnClickRetry : SelectCatEvent
}

sealed interface SelectCatSideEffect : ViewSideEffect {
    data class OnNavToNaming(val no: Int, val catName: String, val catTypeName: String) : SelectCatSideEffect
    data object GoToBack : SelectCatSideEffect
    data class ShowSnackBar(val message: String) : SelectCatSideEffect
}
