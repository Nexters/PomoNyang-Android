package com.pomonyang.mohanyang.data.local.datastore.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeviceIdDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PomodoroDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationDataStore
