package com.pomonyang.data.remote.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultApi
