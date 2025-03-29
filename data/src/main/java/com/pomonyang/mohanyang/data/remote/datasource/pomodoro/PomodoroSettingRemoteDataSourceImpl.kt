package com.pomonyang.mohanyang.data.remote.datasource.pomodoro

import com.pomonyang.mohanyang.data.remote.model.request.AddCategoryRequest
import com.pomonyang.mohanyang.data.remote.model.request.DeleteCategoryRequest
import com.pomonyang.mohanyang.data.remote.model.request.UpdateCategoryInfoRequest
import com.pomonyang.mohanyang.data.remote.model.response.PomodoroSettingResponse
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import javax.inject.Inject

internal class PomodoroSettingRemoteDataSourceImpl @Inject constructor(
    private val mohaNyangService: MohaNyangService,
) : PomodoroSettingRemoteDataSource {

    override suspend fun getPomodoroSettingList(): Result<List<PomodoroSettingResponse>> = mohaNyangService.getPomodoroSettingList()

    override suspend fun modifyCategorySettingOption(
        categoryNo: Int,
        focusTime: String?,
        restTime: String?,
        iconType: String?,
        title: String?,
    ): Result<Unit> = mohaNyangService.updatePomodoroSetting(
        no = categoryNo,
        updateCategoryInfoRequest = UpdateCategoryInfoRequest(
            focusTime = focusTime,
            restTime = restTime,
            iconType = iconType,
            title = title,
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

    override suspend fun deleteCategoryNo(categoryNumbers: List<Int>): Result<Unit> = mohaNyangService.deleteCategories(
        request = DeleteCategoryRequest(categoryNumbers),
    )
}
