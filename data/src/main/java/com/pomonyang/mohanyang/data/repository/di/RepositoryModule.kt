package com.pomonyang.mohanyang.data.repository.di

import com.pomonyang.mohanyang.data.repository.PomodoroSettingRepository
import com.pomonyang.mohanyang.data.repository.impl.PomodoroSettingRepositoryImpl
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsPomodoroSettingRepository(
        pomodoroSettingRepositoryImpl: PomodoroSettingRepositoryImpl
    ): PomodoroSettingRepository

    @Binds
    @Singleton
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
