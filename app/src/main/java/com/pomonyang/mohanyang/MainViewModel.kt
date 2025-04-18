package com.pomonyang.mohanyang

import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse
import com.pomonyang.mohanyang.data.remote.util.BadRequestException
import com.pomonyang.mohanyang.data.remote.util.InternalException
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetTokenByDeviceIdUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withTimeout
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val getTokenByDeviceIdUseCase: GetTokenByDeviceIdUseCase,
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val userRepository: UserRepository,
    private val pushAlarmRepository: PushAlarmRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<MainState, MainEvent, MainEffect>() {

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
    }

    private val scope = viewModelScope + coroutineExceptionHandler

    override fun setInitialState(): MainState = MainState(isLoading = true)

    override fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.Init -> {
                initializeAppData()
                updateState { copy(lastRequestAction = event) }
            }

            MainEvent.ClickRefresh -> {
                onClickRefresh()
                updateState { copy(lastRequestAction = event) }
            }

            MainEvent.ClickClose -> {
                setEffect(MainEffect.ExitApp)
            }

            MainEvent.ClickRetry -> {
                state.value.lastRequestAction?.let { handleEvent(it) }
            }
        }
    }

    private fun initializeAppData() {
        viewModelScope.launch {
            try {
                withTimeout(FETCH_MAX_TIME) {
                    if (networkMonitor.isConnected) {
                        onOnline()
                    } else {
                        onOffline()
                    }
                }
            } catch (e: TimeoutCancellationException) {
                /* MAX TIME 초과시  Cancel 처리된 경우 네트워크 알림처리 */
                setEffect(MainEffect.ShowDialog)
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }

    // 온라인 상태일 때 실행할 초기화 로직
    private fun onOnline() = scope.launch {
        fetchFcmToken()
        fetchUserInfo().onSuccess {
            setupUserAndNavigate(it.isNewUser())
        }.getOrThrow()
    }

    // 오프라인 상태일 때 실행할 초기화 로직
    private fun onOffline() = scope.launch {
        val isNewUser = checkIfNewUser()
        setupUserAndNavigate(isNewUser)
    }

    private suspend fun setupUserAndNavigate(isNewUser: Boolean) {
        if (isNewUser) {
            setEffect(MainEffect.GoToOnBoarding)
        } else {
            savePomodoroCacheData()
            setEffect(MainEffect.GoToTimer)
        }
    }

    fun checkIfNewUser() = userRepository.isNewUser()

    private suspend fun fetchUserInfo(): Result<UserInfoResponse> = runCatching {
        getTokenByDeviceIdUseCase().getOrThrow()
        val userInfo = userRepository.fetchMyInfo().getOrThrow()
        pomodoroSettingRepository.fetchPomodoroSettingList()
        userInfo
    }

    private fun onClickRefresh() {
        if (!networkMonitor.isConnected) return
        scope.launch {
            val userInfo = fetchUserInfo().getOrThrow()
            setEffect(MainEffect.DismissDialog)
            setupUserAndNavigate(userInfo.isNewUser())
        }
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Timber.d("FCM : $token")
                updateFcmToken(token)
            },
        )
    }

    private fun updateFcmToken(token: String) {
        viewModelScope.launch {
            pushAlarmRepository.saveFcmToken(token)
            userRepository.getMyInfo().let {
                if (it.isPushEnabled) {
                    pushAlarmRepository.apply {
                        registerPushToken(token)
                    }
                }
            }
        }
    }

    private suspend fun savePomodoroCacheData() {
        pomodoroTimerRepository.savePomodoroCacheData()
    }

    suspend fun updateRecentPomodoroDoneData() {
        pomodoroTimerRepository.updateRecentPomodoroDone()
    }

    companion object {
        const val FETCH_MAX_TIME = 3000L
    }
}
