package com.pomonyang.mohanyang.data.remote.datasource.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.remote.model.request.AddCategoryRequest
import com.pomonyang.mohanyang.data.remote.model.request.UpdateCategoryInfoRequest
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

internal class PomodoroSettingRemoteDataSourceImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService,
    private val pomodoroSettingDao: PomodoroSettingDao,
) : PomodoroSettingRemoteDataSource {

    override suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>> = mohaNyangService.getPomodoroSettingList()

    override suspend fun updatePomodoroCategoryTimes(
        categoryNo: Int,
        focusTime: String,
        restTime: String,
    ): Result<Unit> = mohaNyangService.updatePomodoroSetting(
        no = categoryNo,
        updateCategoryInfoRequest = UpdateCategoryInfoRequest(
            focusTime = focusTime,
            restTime = restTime,
        ),
    )

    override suspend fun addPomodoroCategory(
        title: String,
        iconType: String,
    ): Result<Unit> = mohaNyangService.addPomodoroCategory(
        addCategoryRequest = AddCategoryRequest(
            title = title,
            iconType = iconType,
        ),
    )
}
