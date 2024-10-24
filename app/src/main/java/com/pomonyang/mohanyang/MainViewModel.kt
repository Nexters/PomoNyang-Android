package com.pomonyang.mohanyang

import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pomonyang.mohanyang.data.remote.model.response.UserInfoResponse
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.domain.usecase.GetTokenByDeviceIdUseCase
import com.pomonyang.mohanyang.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber


@HiltViewModel
class MainViewModel @Inject constructor(
    private val pomodoroTimerRepository: PomodoroTimerRepository,
    private val getTokenByDeviceIdUseCase: GetTokenByDeviceIdUseCase,
    private val pomodoroSettingRepository: PomodoroSettingRepository,
    private val userRepository: UserRepository,
    private val pushAlarmRepository: PushAlarmRepository,
    private val networkMonitor: NetworkMonitor
) : BaseViewModel<MainState, MainEvent, MainEffect>() {


    override fun setInitialState(): MainState {
        return MainState(isLoading = true)
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            MainEvent.Init -> {
                initializeAppData()
            }

            MainEvent.ClickRefresh -> {
                onClickRefresh()
            }

            MainEvent.ClickClose -> {
                setEffect(MainEffect.ExitApp)
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
                    updateState { copy(isLoading = false) }
                }
            } catch (e: TimeoutCancellationException) {
                /* MAX TIME 초과시  Cancel 처리된 경우 네트워크 알림처리 */
                updateState { copy(isLoading = false) }
                setEffect(MainEffect.ShowDialog)
            } catch (e: Exception) {
                Timber.w("${e.message}")
                updateState { copy(isError = true, isLoading = false) }
            }

        }
    }


    // 온라인 상태일 때 실행할 초기화 로직
    private fun onOnline() {
        viewModelScope.launch {
            fetchFcmToken()
            fetchUserInfo().onSuccess {
                setupUserAndNavigate(it.isNewUser())
            }.onFailure {
                updateState { copy(isError = true) }
            }
        }
    }

    // 오프라인 상태일 때 실행할 초기화 로직
    private fun onOffline() {
        viewModelScope.launch {
            val isNewUser = checkIfNewUser()
            setupUserAndNavigate(isNewUser)
        }
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

    private suspend fun fetchUserInfo(): Result<UserInfoResponse> {
        return runCatching {
            getTokenByDeviceIdUseCase().getOrThrow()
            val userInfo = userRepository.fetchMyInfo().getOrThrow()
            pomodoroSettingRepository.fetchPomodoroSettingList()
            userInfo
        }
    }

    private fun onClickRefresh() {
        if (!networkMonitor.isConnected) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            fetchUserInfo().onSuccess {
                setEffect(MainEffect.DismissDialog)
                setupUserAndNavigate(it.isNewUser())
            }.onFailure {
                updateState { copy(isError = true) }
            }

            updateState { copy(isLoading = false) }
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
            }
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
