package com.pomonyang.mohanyang.presentation.di

import com.pomonyang.mohanyang.presentation.util.MohanyangEventLogger
import com.pomonyang.mohanyang.presentation.util.MohanyangEventLoggerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MohanyangLoggerModule {

    @Binds
    @Singleton
    abstract fun provideMohanyangEventLogger(
        mohanyangEventLoggerImpl: MohanyangEventLoggerImpl,
    ): MohanyangEventLogger
}
