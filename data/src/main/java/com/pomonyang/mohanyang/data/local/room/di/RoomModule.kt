package com.pomonyang.mohanyang.data.local.room.di

import android.content.Context
import androidx.room.Room
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroSettingDao
import com.pomonyang.mohanyang.data.local.room.dao.PomodoroTimerDao
import com.pomonyang.mohanyang.data.local.room.database.PomodoroSettingDataBase
import com.pomonyang.mohanyang.data.local.room.database.PomodoroTimerDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {
    @Provides
    @Singleton
    fun providePomodoroSettingDataBase(
        @ApplicationContext context: Context
    ): PomodoroSettingDataBase = Room.databaseBuilder(
        context = context,
        klass = PomodoroSettingDataBase::class.java,
        name = "pomodoro-category-database" // database name string 관리 필요
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePomodoroTimerDataBase(
        @ApplicationContext context: Context
    ): PomodoroTimerDataBase = Room
        .databaseBuilder(
            context = context,
            klass = PomodoroTimerDataBase::class.java,
            name = "pomodoro-timer-database" // database name string 관리 필요
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePomodoroSettingDao(database: PomodoroSettingDataBase): PomodoroSettingDao = database.dao()

    @Provides
    @Singleton
    fun providePomodoroTimerDao(database: PomodoroTimerDataBase): PomodoroTimerDao = database.dao()
}
