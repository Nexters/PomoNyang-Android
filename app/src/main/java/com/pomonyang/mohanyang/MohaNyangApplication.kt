package com.pomonyang.mohanyang

import android.app.Application
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
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
        val environmentName = if (BuildConfig.DEBUG) "dev" else "prod"
        val appVariantName = "${getString(R.string.app_name)}[${BuildConfig.VERSION_NAME}]"

        val configuration = Configuration.Builder(
            clientToken = clientToken,
            env = environmentName,
            variant = appVariantName
        )
            .useSite(DatadogSite.US5)
            .build()

        Datadog.initialize(this, configuration, TrackingConsent.GRANTED)

        val applicationId = BuildConfig.APPLICATION_ID
        val rumConfiguration = RumConfiguration.Builder(applicationId)
            .trackUserInteractions()
            .build()

        Rum.enable(rumConfiguration)
    }
}
