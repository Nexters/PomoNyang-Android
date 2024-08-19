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
    data object ClickCatProfile : MyPageEvent
    data class ChangeInterruptNotification(val isEnabled: Boolean) : MyPageEvent
    data class ChangeTimerNotification(val isEnabled: Boolean) : MyPageEvent
    data object CloseDialog : MyPageEvent
    data object OpenSetting : MyPageEvent
    data object ClickSuggestion : MyPageEvent
}

sealed interface MyPageSideEffect : ViewSideEffect {
    data object GoToCatProfilePage : MyPageSideEffect
    data class CheckNotificationPermission(val request: NotificationRequest, val onGranted: () -> Unit) : MyPageSideEffect
    data class OpenExternalWebPage(val url: String) : MyPageSideEffect
    data object CloseDialog : MyPageSideEffect
    data object OpenDialog : MyPageSideEffect
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
                setEffect(MyPageSideEffect.GoToCatProfilePage)
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

            MyPageEvent.ClickSuggestion -> {
                setEffect(MyPageSideEffect.OpenExternalWebPage("https://forms.gle/wEUPH9Tvxgua4hCZ9"))
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
