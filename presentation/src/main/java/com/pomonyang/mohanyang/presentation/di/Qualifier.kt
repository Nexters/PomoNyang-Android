package com.pomonyang.mohanyang.presentation.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PomodoroNotification

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FocusTimerType

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RestTimerType
