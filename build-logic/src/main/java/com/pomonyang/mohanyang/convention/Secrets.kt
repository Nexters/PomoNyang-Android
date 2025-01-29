package com.pomonyang.mohanyang.convention

import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin?hl=ko
internal fun Project.configureSecret() {
    val releasePropertiesName = "release.secrets.properties"
    val debugPropertiedName = "debug.secrets.properties"
    extensions.configure<SecretsPluginExtension> {
        val isDebug = project.gradle.startParameter.taskNames.any { it.contains("Debug", ignoreCase = true) }
        propertiesFileName = if (isDebug) debugPropertiedName else releasePropertiesName
        ignoreList.add("keyToIgnore")
        ignoreList.add("sdk.*")
    }
}
