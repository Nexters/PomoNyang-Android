package com.pomonyang.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE : ViewState, EVENT : ViewEvent, EFFECT : ViewSideEffect> : ViewModel() {
    // UI의 초기 상태를 설정
    abstract fun setInitialState(): STATE

    abstract suspend fun handleEvent(event: EVENT)

    // 초기 상태를 지연 초기화
    private val initialState: STATE by lazy { setInitialState() }

    private val _state = MutableStateFlow<STATE>(initialState)
    val state = _state.asStateFlow()

    @Suppress("ktlint:standard:backing-property-naming")
    private val _events: Channel<EVENT> = Channel<EVENT>()

    @Suppress("ktlint:standard:backing-property-naming")
    private val _effects: Channel<EFFECT> = Channel<EFFECT>()
    val effect: Flow<EFFECT> = _effects.receiveAsFlow()

    init {
        subscribeToEvents()
        onInit()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            for (event in _events) {
                handleEvent(event)
            }
        }
    }

    // 추가적인 viewModel initial 작업
    protected open fun onInit() {}

    fun setEvent(event: EVENT) {
        viewModelScope.launch { _events.send(event) }
    }

    protected fun setState(reducer: STATE.() -> STATE) {
        val newState = _state.value.reducer()
        _state.value = newState
    }

    // Intent -> Model -> View 의 사이클을 벗어난 비동기 작업이 완료된 후 UI 상태 변경 외의 작업을 수행할 때 사용
    protected fun setEffect(vararg builder: EFFECT) {
        viewModelScope.launch {
            for (effectValue in builder) {
                _effects.send(effectValue)
            }
        }
    }

    // viewmodel이 destroy 될 때 추가적인 작업이 필요하다면
    protected open fun onDestroy() {}

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        onDestroy()
        _events.close()
        _effects.close()
        _events.close()
    }
}
