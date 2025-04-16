package com.pomonyang.mohanyang.presentation.screen.mypage

import com.pomonyang.mohanyang.presentation.base.ViewEvent
import com.pomonyang.mohanyang.presentation.base.ViewSideEffect
import com.pomonyang.mohanyang.presentation.base.ViewState

data class MyPageState(
    val catName: String = "",
    val isInterruptNotificationEnabled: Boolean = false,
    val isTimerNotificationEnabled: Boolean = false,
    val isLockScreenNotificationEnabled: Boolean = false,
) : ViewState

sealed interface MyPageEvent : ViewEvent {
    data class Init(val appNotificationGranted: Boolean) : MyPageEvent
    data class ClickCatProfile(val isOffline: Boolean) : MyPageEvent
    data class ChangeInterruptNotification(val isEnabled: Boolean) : MyPageEvent
    data class ChangeTimerNotification(val isEnabled: Boolean) : MyPageEvent
    data class ChangeLockScreenNotification(val isEnabled: Boolean) : MyPageEvent
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
    data class ShowSnackBar(val message: String) : MyPageSideEffect
}
