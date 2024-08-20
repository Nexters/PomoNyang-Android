package com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro

import kotlinx.coroutines.flow.Flow

interface PomodoroLocalDataSource {
    fun getRecentCategoryNo(): Flow<Int>
    suspend fun updateRecentCategoryNo(categoryNo: Int)
}
