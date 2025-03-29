package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroSettingEntity
import com.pomonyang.mohanyang.data.remote.datasource.pomodoro.PomodoroSettingRemoteDataSource
import com.pomonyang.mohanyang.data.remote.model.response.toEntity
import java.time.Duration
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEmpty

internal class PomodoroSettingRepositoryImpl @Inject constructor(
    private val pomodoroSettingRemoteDataSource: PomodoroSettingRemoteDataSource,
    private val pomodoroSettingDao: PomodoroSettingDao,
) : PomodoroSettingRepository {

    override fun getPomodoroSettingList(): Flow<List<PomodoroSettingEntity>> = pomodoroSettingDao
        .getPomodoroSetting()
        .onEmpty { fetchPomodoroSettingList() }

    override fun getSelectedPomodoroSetting(): Flow<PomodoroSettingEntity> {
        return pomodoroSettingDao.getSelectedPomodoroSetting().flatMapLatest { selected ->
            if (selected != null) {
                flowOf(selected)
            } else {
                // 임시 코드 아무것도 선택 안한 유저에게는 첫번째 카테고리로 설정이 되게 해줄 수 있는 확인 필요
                flow {
                    emit(getPomodoroSettingList().first().first())
                }
            }
        }
    }

    override suspend fun fetchPomodoroSettingList() {
        pomodoroSettingRemoteDataSource.getPomodoroSettingList()
            .onSuccess { responses ->
                pomodoroSettingDao.insertPomodoroSettingData(
                    responses.map { response -> response.toEntity() },
                )
            }
    }

    override suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        focusTime: Int,
        restTime: Int,
    ): Result<Unit> {
        val focusTimeDuration = Duration.ofMinutes(focusTime.toLong()).toString()
        val restTimeDuration = Duration.ofMinutes(restTime.toLong()).toString()
        updateLocalPomodoroSetting(categoryNo, focusTimeDuration, restTimeDuration)
        return pomodoroSettingRemoteDataSource.updatePomodoroCategoryTimes(
            categoryNo = categoryNo,
            focusTime = focusTimeDuration,
            restTime = restTimeDuration,
        )
    }

    private suspend fun updateLocalPomodoroSetting(
        categoryNo: Int,
        focusTime: String,
        restTime: String,
    ) {
        pomodoroSettingDao.updateTimes(
            categoryNo = categoryNo,
            focusTime = focusTime,
            restTime = restTime,
        )
    }

    override suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit> {
        return pomodoroSettingRemoteDataSource.addPomodoroCategory(
            title = title,
            iconType = iconType,
        ).onSuccess {
            fetchPomodoroSettingList()
        }
    }


    override suspend fun updateRecentUseCategoryNo(categoryNo: Int) {
        pomodoroSettingDao.updateSelectCategory(categoryNo)
    }
}
