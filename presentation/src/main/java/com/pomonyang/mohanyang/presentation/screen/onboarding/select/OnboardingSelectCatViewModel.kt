package com.pomonyang.mohanyang.presentation.screen.onboarding.select

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.repository.cat.CatSettingRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.NetworkViewState
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.model.cat.CatInfoModel
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.model.cat.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber


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
                updateState { copy(lastRequestAction = event) }
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
                updateState { copy(lastRequestAction = event) }
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
        viewModelScope.launch {
            try {
                catSettingRepository.getCatTypes().onSuccess { cats ->
                    val catList = cats.map { it.toModel() }
                    updateState {
                        copy(
                            cats = catList,
                            selectedType = catList.find { it.no == selectedCatNo }?.type,
                        )
                    }
                }.getOrThrow()
            } catch (e: BadRequestException) {
                updateState { copy(isInvalidError = true) }
            } catch (e: Exception) {
                Timber.e(e)
                setEffect(SelectCatSideEffect.GoToBack, SelectCatSideEffect.ShowSnackBar("고양이 선택을 위해 네트워크 연결이 필요해요"))
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun updateSelectCatType(catNo: Int) {
        viewModelScope.launch {
            try {
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
            } catch (e: BadRequestException) {
                setEffect(SelectCatSideEffect.ShowSnackBar("일시적 오류가 발생하였습니다."))
                Timber.e(e)
            } catch (e: Exception) {
                updateState { copy(isInvalidError = true) }
                Timber.e(e)
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }


}
