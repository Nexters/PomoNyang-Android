package com.pomonyang.mohanyang.presentation.screen.mypage.profile

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class CatProfileState(
    val catName: String = "",
    val catType: CatType = CatType.CHEESE,
    val catNo: Int = -1
) : ViewState

sealed interface CatProfileEvent : ViewEvent {
    data object Init : CatProfileEvent
    data object ClickNaming : CatProfileEvent
    data object ClickChangeType : CatProfileEvent
}

sealed interface CatProfileSideEffect : ViewSideEffect {
    data class GoToCatNaming(val catName: String, val catNo: Int) : CatProfileSideEffect
    data class GoToCatTypeChange(val catNo: Int) : CatProfileSideEffect
}

@HiltViewModel
class CatProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<CatProfileState, CatProfileEvent, CatProfileSideEffect>() {
    override fun setInitialState(): CatProfileState = CatProfileState()

    override fun handleEvent(event: CatProfileEvent) {
        when (event) {
            is CatProfileEvent.Init -> {
                getMyInfo()
            }

            is CatProfileEvent.ClickNaming -> {
                with(state.value) {
                    setEffect(CatProfileSideEffect.GoToCatNaming(catName, catNo))
                }
            }

            is CatProfileEvent.ClickChangeType -> {
                setEffect(CatProfileSideEffect.GoToCatTypeChange(state.value.catNo))
            }
        }
    }

    private fun getMyInfo() {
        viewModelScope.launch {
            userRepository.fetchMyInfo().onSuccess {
                updateState { copy(catName = it.cat.name, catNo = it.cat.no, catType = CatType.safeValueOf(it.cat.type)) }
            }
        }
    }
}
