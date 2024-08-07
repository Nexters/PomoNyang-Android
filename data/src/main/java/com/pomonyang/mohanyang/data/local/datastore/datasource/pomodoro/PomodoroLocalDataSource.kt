package com.pomonyang.mohanyang.data.local.datastore.datasource.pomodoro

interface PomodoroLocalDataSource {
    suspend fun getRecentCategoryNo(): Int
    suspend fun updateRecentCategoryNo(categoryNo: Int)
}
