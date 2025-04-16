package com.pomonyang.mohanyang.presentation.screen.mypage

import androidx.lifecycle.viewModelScope
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val pushAlarmRepository: PushAlarmRepository,
    private val userRepository: UserRepository,
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
                        onGranted = { setITimerNotification(event.isEnabled) },
                    ),
                )
            }

            is MyPageEvent.ChangeInterruptNotification -> {
                setEffect(
                    MyPageSideEffect.CheckNotificationPermission(
                        request = NotificationRequest.INTERRUPT,
                        onGranted = { setInterruptNotification(event.isEnabled) },
                    ),
                )
            }

            is MyPageEvent.ChangeLockScreenNotification -> {
                setEffect(
                    MyPageSideEffect.CheckNotificationPermission(
                        request = NotificationRequest.LOCKSCREEN,
                        onGranted = { setLockScreenNotification(event.isEnabled) },
                    ),
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

    private fun setLockScreenNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            pushAlarmRepository.setLockScreenNotification(isEnabled)
            updateState { copy(isLockScreenNotificationEnabled = isEnabled) }
        }
    }

    private fun getUserProfile(appNotificationGranted: Boolean) {
        viewModelScope.launch {
            val userInfo = userRepository.getMyInfo()

            val isInterruptEnabled = appNotificationGranted && pushAlarmRepository.isInterruptNotificationEnabled()
            val isTimerEnabled = appNotificationGranted && pushAlarmRepository.isTimerNotificationEnabled()
            val isLockScreenEnabled = appNotificationGranted && pushAlarmRepository.isLockScreenNotificationEnabled()

            updateState {
                copy(
                    catName = userInfo.cat.name,
                    isInterruptNotificationEnabled = isInterruptEnabled,
                    isTimerNotificationEnabled = isTimerEnabled,
                    isLockScreenNotificationEnabled = isLockScreenEnabled,
                )
            }
        }
    }
}
