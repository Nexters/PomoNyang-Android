package com.pomonyang

import android.app.Application
import com.pomonyang.ui.DebugTimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PomoNyangApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTimberTree())
    }
}
