package com.pomonyang.mohanyang.presentation.di

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
    fun provideFocusTimer(): PomodoroTimer = FocusTimer()

    @Provides
    @RestTimerType
    fun provideRestTimer(): PomodoroTimer = RestTimer()
}
