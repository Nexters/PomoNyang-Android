package com.pomonyang.mohanyang.presentation.screen.mypage

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class MyPageState(
    val catName: String = "",
    val isInterruptNotificationEnabled: Boolean = false,
    val isTimerNotificationEnabled: Boolean = false
) : ViewState

sealed interface MyPageEvent : ViewEvent {
    data class Init(val appNotificationGranted: Boolean) : MyPageEvent
    data class ClickCatProfile(val isOffline: Boolean) : MyPageEvent
    data class ChangeInterruptNotification(val isEnabled: Boolean) : MyPageEvent
    data class ChangeTimerNotification(val isEnabled: Boolean) : MyPageEvent
    data object CloseDialog : MyPageEvent
    data object OpenSetting : MyPageEvent
    data object ClickSuggestion : MyPageEvent
    data object ClickStatic : MyPageEvent
}

sealed interface MyPageSideEffect : ViewSideEffect {
    data object GoToCatProfilePage : MyPageSideEffect
    data class CheckNotificationPermission(val request: NotificationRequest, val onGranted: () -> Unit) : MyPageSideEffect
    data class OpenExternalWebPage(val url: String) : MyPageSideEffect
    data object CloseDialog : MyPageSideEffect
    data object OpenDialog : MyPageSideEffect
    data class ShowSnackBar(val message: String) : MyPageSideEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val pushAlarmRepository: PushAlarmRepository,
    private val userRepository: UserRepository
) : BaseViewModel<MyPageState, MyPageEvent, MyPageSideEffect>() {
    override fun setInitialState(): MyPageState = MyPageState()

    override fun handleEvent(event: MyPageEvent) {
        when (event) {
            is MyPageEvent.Init -> {
                getUserProfile(event.appNotificationGranted)
            }

            is MyPageEvent.ClickCatProfile -> {
                if (event.isOffline) {
                    setEffect(MyPageSideEffect.ShowSnackBar("오프라인 모드에서 고양이를 변경할 수 없습니다"))
                } else {
                    setEffect(MyPageSideEffect.GoToCatProfilePage)
                }
            }

            is MyPageEvent.ChangeTimerNotification -> {
                setEffect(
                    MyPageSideEffect.CheckNotificationPermission(
                        request = NotificationRequest.TIMER,
                        onGranted = { setITimerNotification(event.isEnabled) }
                    )
                )
            }

            is MyPageEvent.ChangeInterruptNotification -> {
                setEffect(
                    MyPageSideEffect.CheckNotificationPermission(
                        request = NotificationRequest.INTERRUPT,
                        onGranted = { setInterruptNotification(event.isEnabled) }
                    )
                )
            }

            is MyPageEvent.CloseDialog -> {
                setEffect(MyPageSideEffect.CloseDialog)
            }

            is MyPageEvent.OpenSetting -> {
                setEffect(MyPageSideEffect.OpenDialog)
            }

            is MyPageEvent.ClickSuggestion -> {
                setEffect(MyPageSideEffect.OpenExternalWebPage("https://forms.gle/wEUPH9Tvxgua4hCZ9"))
            }

            is MyPageEvent.ClickStatic -> {
                setEffect(MyPageSideEffect.ShowSnackBar("통계를 사용할 수 있도록 준비하고 있어요"))
            }
        }
    }

    private fun setITimerNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            pushAlarmRepository.setTimerNotification(isEnabled)
            updateState { copy(isTimerNotificationEnabled = isEnabled) }
        }
    }

    private fun setInterruptNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            pushAlarmRepository.setInterruptNotification(isEnabled)
            updateState { copy(isInterruptNotificationEnabled = isEnabled) }
        }
    }

    private fun getUserProfile(appNotificationGranted: Boolean) {
        viewModelScope.launch {
            val userInfo = userRepository.getMyInfo()

            val isInterruptEnabled = appNotificationGranted && pushAlarmRepository.isInterruptNotificationEnabled()
            val isTimerEnabled = appNotificationGranted && pushAlarmRepository.isTimerNotificationEnabled()

            updateState {
                copy(
                    catName = userInfo.cat.name,
                    isInterruptNotificationEnabled = isInterruptEnabled,
                    isTimerNotificationEnabled = isTimerEnabled
                )
            }
        }
    }
}
