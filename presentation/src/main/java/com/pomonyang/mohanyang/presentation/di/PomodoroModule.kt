package com.pomonyang.mohanyang.presentation.di

import com.pomonyang.mohanyang.data.repository.pomodoro.PomodoroTimerRepository
import com.pomonyang.mohanyang.presentation.service.PomodoroTimer
import com.pomonyang.mohanyang.presentation.service.focus.FocusTimer
import com.pomonyang.mohanyang.presentation.service.rest.RestTimer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
internal object PomodoroModule {

    @Provides
    @FocusTimerType
    fun provideFocusTimer(
        timerRepository: PomodoroTimerRepository
    ): PomodoroTimer = FocusTimer(timerRepository = timerRepository)

    @Provides
    @RestTimerType
    fun provideRestTimer(
        timerRepository: PomodoroTimerRepository
    ): PomodoroTimer = RestTimer(timerRepository = timerRepository)
}
