package com.pomonyang.mohanyang

import android.app.Activity
import android.app.Application
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy
import com.datadog.android.rum.tracking.ComponentPredicate
import com.pomonyang.mohanyang.util.DebugTimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MohaNyangApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTimberTree())
        initializeDataDog()
    }

    private fun initializeDataDog() {
        val clientToken = BuildConfig.DATADOG_CLIENT_TOKEN
        val environmentName = DATADOG_ENV
        val appVariantName = BuildConfig.DATADOG_APP_VARIANT

        val configuration = Configuration.Builder(
            clientToken = clientToken,
            env = environmentName,
            variant = appVariantName
        )
            .useSite(DatadogSite.US5)
            .build()

        Datadog.initialize(this, configuration, TrackingConsent.GRANTED)

        val applicationId = BuildConfig.DATADOG_APPLICATION_ID
        val rumConfiguration = RumConfiguration.Builder(applicationId)
            .trackUserInteractions()
            .trackLongTasks(250L)
            .useViewTrackingStrategy(
                ActivityViewTrackingStrategy(
                    trackExtras = true,
                    componentPredicate = object : ComponentPredicate<Activity> {
                        override fun accept(component: Activity): Boolean = component !is MainActivity

                        override fun getViewName(component: Activity): String? = null
                    }
                )
            )
            .build()

        Rum.enable(rumConfiguration)
    }

    companion object {
        private val DATADOG_ENV = if (BuildConfig.DEBUG) "dev" else "prod"
    }
}
