package com.pomonyang.mohanyang.data.repository.pomodoro

interface PomodoroTimerRepository {
    suspend fun insertPomodoroTimerInitData(categoryNo: Int)
    suspend fun updateFocusTime(focusedTime: Int)
    suspend fun updateRestedTime(restedTime: Int)
    suspend fun updatePomodoroDone()
    suspend fun savePomodoroCacheData()
}
