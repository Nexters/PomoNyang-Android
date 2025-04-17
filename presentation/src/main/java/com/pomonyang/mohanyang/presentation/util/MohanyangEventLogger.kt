package com.pomonyang.mohanyang.presentation.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mohanyang.presentation.BuildConfig
import com.pomonyang.mohanyang.data.local.datastore.datasource.deviceid.DeviceIdLocalDataSource
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

interface MohanyangEventLogger {
    fun log(event: MohanyangEventLog)
}

class MohanyangEventLoggerImpl @Inject constructor(
    private val deviceIdLocalDataSource: DeviceIdLocalDataSource,
) : MohanyangEventLogger {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
    private val userDeviceId by lazy {
        runBlocking { deviceIdLocalDataSource.getDeviceId() }
    }

    override fun log(event: MohanyangEventLog) {
        val userId = userDeviceId
        val defaultParams = mapOf(
            "user_id" to userId,
            "timestamp" to LocalDateTime.now(),
            "os" to "android",
            "debug" to "${BuildConfig.DEBUG}"
        )
        val params = defaultParams + event.toParams()
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Double -> putDouble(key, value)
                    else -> putString(key, value?.toString())
                }
            }
        }
        firebaseAnalytics.logEvent(event.name, bundle)
        Timber.d("MohanyangLogger Logged [${event.name}] : $params")
    }
}

sealed class MohanyangEventLog(val name: String) {
    open fun toParams(): Map<String, Any?> = emptyMap()

    data object HomeCategoryClick : MohanyangEventLog("user_home_category_click")

    data object CategoryAddClick : MohanyangEventLog("user_category_add_click")

    data class CategoryCreateCount(
        val count: Int,
    ) : MohanyangEventLog("user_category_create_count") {
        override fun toParams() = mapOf("count" to count)
    }

    data object CategoryEditClick : MohanyangEventLog("category_edit_click")

    data object CategoryEditComplete : MohanyangEventLog("category_edit_complete")

    data class CategoryIconUsage(
        val iconType: String,
    ) : MohanyangEventLog(
        "category_icon_usage",
    ) {
        override fun toParams() = mapOf("iconType" to iconType)
    }

    data object CategoryDeleteCTAClick : MohanyangEventLog("category_delete_cta_click")

    data class CategoryDeletedCount(
        val count: Int,
    ) : MohanyangEventLog("deleted_category_count") {
        override fun toParams() = mapOf("count" to count)
    }
}
