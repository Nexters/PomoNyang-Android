package com.pomonyang.mohanyang.data.repository.impl

import com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro.PomodoroLocalDataSource
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.local.room.enitity.toResponse
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import com.pomonyang.mohanyang.data.remote.model.response.toEntity
import com.pomonyang.mohanyang.data.repository.PomodoroSettingRepository
import javax.inject.Inject

internal class PomodoroSettingRepositoryImpl @Inject constructor(
    private val pomodoroSettingRemoteDataSource: PomodoroSettingRemoteDataSource,
    private val pomodoroLocalDataSource: PomodoroLocalDataSource,
    private val pomodoroSettingDao: PomodoroSettingDao
) : PomodoroSettingRepository {

    override suspend fun getRecentUseCategoryNo(): Int = pomodoroLocalDataSource.getRecentCategoryNo()

    override suspend fun updateRecentUseCategoryNo(categoryNo: Int) {
        pomodoroLocalDataSource.updateRecentCategoryNo(categoryNo)
    }

    override suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>> = pomodoroSettingRemoteDataSource.getPomodoroSettingList()
        .onSuccess { response ->
            pomodoroSettingDao.insertPomodoroSettingData(response.map { it.toEntity() })
        }
        .recoverCatching {
            pomodoroSettingDao.getPomodoroSetting().map { it.toResponse() }
        }

    override suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        titleName: String,
        focusTime: String,
        restTime: String
    ): Result<Unit> {
        updateLocalPomodoroSetting(categoryNo, titleName, focusTime, restTime)
        return pomodoroSettingRemoteDataSource.updatePomodoroCategoryTimes(
            categoryNo = categoryNo,
            focusTime = focusTime,
            restTime = restTime
        )
    }

    private suspend fun updateLocalPomodoroSetting(categoryNo: Int, titleName: String, focusTime: String, restTime: String) {
        pomodoroSettingDao.updatePomodoroSettingData(
            pomodoroSettingEntity = PomodoroSettingEntity(
                categoryNo = categoryNo,
                title = titleName,
                focusTime = focusTime,
                restTime = restTime
            )
        )
    }
}
