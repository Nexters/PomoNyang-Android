package com.pomonyang.mohanyang

import android.app.Application
import com.pomonyang.mohanyang.util.DebugTimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MohaNyangApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTimberTree())
    }
}
