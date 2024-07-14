package com.pomonyang.convention

import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin?hl=ko
internal fun Project.configureSecret() {
    val releasePropertiesName = "release.secrets.properties"
    val debugPropertiedName = "debug.secrets.properties"
    extensions.configure<SecretsPluginExtension> {
        propertiesFileName = releasePropertiesName
        defaultPropertiesFileName = debugPropertiedName
        ignoreList.add("keyToIgnore")
        ignoreList.add("sdk.*")
    }
}
