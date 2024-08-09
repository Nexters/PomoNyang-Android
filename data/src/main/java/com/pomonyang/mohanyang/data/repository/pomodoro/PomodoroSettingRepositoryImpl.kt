package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro.PomodoroLocalDataSource
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.model.response.toEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEmpty

internal class PomodoroSettingRepositoryImpl @Inject constructor(
    private val pomodoroSettingRemoteDataSource: PomodoroSettingRemoteDataSource,
    private val pomodoroLocalDataSource: PomodoroLocalDataSource,
    private val pomodoroSettingDao: PomodoroSettingDao
) : PomodoroSettingRepository {

    override fun getRecentUseCategoryNo(): Flow<Int> = pomodoroLocalDataSource.getRecentCategoryNo()

    override suspend fun updateRecentUseCategoryNo(categoryNo: Int) {
        pomodoroLocalDataSource.updateRecentCategoryNo(categoryNo)
    }

    override fun getPomodoroSettingList(): Flow<List<PomodoroSettingEntity>> = pomodoroSettingDao
        .getPomodoroSetting()
        .onEmpty { fetchPomodoroSettingList() }

    override suspend fun fetchPomodoroSettingList() {
        pomodoroSettingRemoteDataSource.getPomodoroSettingList()
            .also {
                it.onSuccess { responses ->
                    pomodoroSettingDao.insertPomodoroSettingData(
                        responses.map { response -> response.toEntity() }
                    )
                }
            }
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