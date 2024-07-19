package com.pomonyang.data.local.room.di

import android.content.Context
import androidx.room.Room
import com.pomonyang.data.local.room.dao.SampleDao
import com.pomonyang.data.local.room.database.SampleDataBase
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
    fun provideSampleDatabase(
        @ApplicationContext context: Context
    ): SampleDataBase =
        Room
            .databaseBuilder(
                context,
                SampleDataBase::class.java,
                name = "sample-database" // database name string 관리 필요
            ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSampleDao(database: SampleDataBase): SampleDao = database.dao()
}
