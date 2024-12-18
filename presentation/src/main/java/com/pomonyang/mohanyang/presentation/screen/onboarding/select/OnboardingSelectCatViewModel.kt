package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.remote.util.InternalException
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@HiltViewModel
class OnboardingSelectCatViewModel @Inject constructor(
    private val catSettingRepository: CatSettingRepository,
    private val pushAlarmRepository: PushAlarmRepository,
    private val userRepository: UserRepository
) : BaseViewModel<SelectCatState, SelectCatEvent, SelectCatSideEffect>() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is InternalException -> {
                updateState { copy(isInternalError = true) }
            }

            is BadRequestException -> {
                updateState { copy(isInvalidError = true) }
            }

            else -> {
                updateState { copy(isInvalidError = true) }
            }
        }
        updateState { copy(isLoading = false) }
    }

    private val scope = viewModelScope + coroutineExceptionHandler

    override fun setInitialState(): SelectCatState = SelectCatState()

    override fun handleEvent(event: SelectCatEvent) {
        when (event) {
            is SelectCatEvent.Init -> {
                getCatTypes(event.catNo)
                updateState { copy(lastRequestAction = event) }
            }

            is SelectCatEvent.OnSelectType -> {
                updateState { copy(selectedType = event.type) }
            }

            is SelectCatEvent.OnStartClick -> {
                updateState { copy(lastRequestAction = event) }
                with(state.value) {
                    cats.find { it.type == selectedType }?.no
                }?.let { selectedNo ->
                    updateSelectCatType(selectedNo)
                }
            }

            is SelectCatEvent.OnGrantedAlarmPermission -> {
                viewModelScope.launch {
                    pushAlarmRepository.setInterruptNotification(true)
                    pushAlarmRepository.setTimerNotification(true)
                }
            }

            is SelectCatEvent.OnClickRetry -> {
                state.value.lastRequestAction?.let { handleEvent(it) }
            }
        }
    }

    private fun getCatTypes(selectedCatNo: Int?) {
        scope.launch {
            updateState { copy(isLoading = true) }
            catSettingRepository.getCatTypes().onSuccess { cats ->
                val catList = cats.map { it.toModel() }
                updateState {
                    copy(
                        cats = catList,
                        selectedType = catList.find { it.no == selectedCatNo }?.type
                    )
                }
            }.getOrThrow()
            updateState {
                copy(
                    isLoading = false,
                    isInternalError = false,
                    isInvalidError = false
                )
            }
        }
    }

    private fun updateSelectCatType(catNo: Int) {
        scope.launch {
            updateState { copy(isLoading = true) }
            catSettingRepository.updateCatType(catNo).getOrThrow()
            userRepository.fetchMyInfo().getOrThrow()
            val cat = state.value.cats.find { it.no == catNo }
            setEffect(
                SelectCatSideEffect.OnNavToNaming(
                    catNo,
                    cat?.name ?: "",
                    state.value.selectedType?.name ?: ""
                )
            )
            updateState {
                copy(
                    isLoading = false,
                    isInvalidError = false,
                    isInternalError = false
                )
            }
        }
    }
}
