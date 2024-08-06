package com.pomonyang.mohanyang.data.repository.impl

import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import com.pomonyang.mohanyang.data.repository.PomodoroSettingRepository
import javax.inject.Inject

internal class PomodoroSettingRepositoryImpl @Inject constructor(
    private val pomodoroSettingRemoteDataSource: PomodoroSettingRemoteDataSource
) : PomodoroSettingRepository {

    override suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>> = pomodoroSettingRemoteDataSource.getPomodoroSettingList()

    override suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        focusTime: String,
        restTime: String
    ): Result<Unit> = updatePomodoroCategoryTimes(
        categoryNo = categoryNo,
        focusTime = focusTime,
        restTime = restTime
    )
}
