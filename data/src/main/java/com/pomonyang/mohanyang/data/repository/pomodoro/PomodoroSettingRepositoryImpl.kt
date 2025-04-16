package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.model.response.toEntity
import java.time.Duration
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEmpty

internal class PomodoroSettingRepositoryImpl @Inject constructor(
    private val pomodoroSettingRemoteDataSource: PomodoroSettingRemoteDataSource,
    private val pomodoroSettingDao: PomodoroSettingDao,
) : PomodoroSettingRepository {

    override fun getPomodoroSettingList(): Flow<List<PomodoroSettingEntity>> = pomodoroSettingDao
        .getPomodoroSetting()
        .onEmpty { fetchPomodoroSettingList() }

    override fun getSelectedPomodoroSetting(): Flow<PomodoroSettingEntity?> = pomodoroSettingDao.getSelectedPomodoroSetting()

    override suspend fun fetchPomodoroSettingList() {
        pomodoroSettingRemoteDataSource.getPomodoroSettingList()
            .onSuccess { responses ->
                pomodoroSettingDao.deleteAllPomodoroSettings()
                pomodoroSettingDao.insertPomodoroSettingData(
                    responses.map { response -> response.toEntity() },
                )
            }
    }

    override suspend fun updatePomodoroCategorySetting(
        categoryNo: Int,
        title: String?,
        iconType: String?,
        focusTime: Int?,
        restTime: Int?,
    ): Result<Unit> {
        val focusTimeDuration = focusTime?.let {
            Duration.ofMinutes(it.toLong()).toString()
        }

        val restTimeDuration = restTime?.let {
            Duration.ofMinutes(it.toLong()).toString()
        }
        return pomodoroSettingRemoteDataSource.modifyCategorySettingOption(
            categoryNo = categoryNo,
            focusTime = focusTimeDuration,
            restTime = restTimeDuration,
            iconType = iconType,
            title = title,
        ).onSuccess {
            updateLocalPomodoroSetting(
                categoryNo,
                focusTimeDuration,
                restTimeDuration,
                iconType,
                title,
            )
        }
    }

    private suspend fun updateLocalPomodoroSetting(
        categoryNo: Int,
        focusTime: String?,
        restTime: String?,
        iconType: String?,
        title: String?,
    ) {
        pomodoroSettingDao.updateSetting(
            categoryNo = categoryNo,
            focusTime = focusTime,
            restTime = restTime,
            iconType = iconType,
            title = title,
        )
    }

    override suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit> = pomodoroSettingRemoteDataSource.addPomodoroCategory(
        title = title,
        iconType = iconType,
    ).onSuccess {
        fetchPomodoroSettingList()
    }

    override suspend fun updateRecentUseCategoryNo(categoryNo: Int) {
        pomodoroSettingRemoteDataSource.updateSelectPomodoroCategory(categoryNo).onSuccess {
            pomodoroSettingDao.updateSelectCategory(categoryNo)
        }
    }

    override suspend fun deleteCategories(categoryNumbers: List<Int>): Result<Unit> = pomodoroSettingRemoteDataSource.deleteCategories(categoryNumbers).onSuccess {
        pomodoroSettingDao.deletePomodoroSettingsByCategoryNos(categoryNumbers)
    }
}
