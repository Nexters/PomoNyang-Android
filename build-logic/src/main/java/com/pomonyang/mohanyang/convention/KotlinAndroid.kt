package com.pomonyang.mohanyang.convention

import com.android.build.api.dsl.CommonExtension
import com.pomonyang.mohanyang.convention.ProjectConfigurations.JAVA_VERSION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = ProjectConfigurations.COMPILE_SDK

        defaultConfig {
            minSdk = ProjectConfigurations.MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            resourceConfigurations.addAll(listOf("en", "ko"))
        }

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }

        buildFeatures {
            buildConfig = true
        }

        kotlinOptions {
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            freeCompilerArgs = freeCompilerArgs +
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )

            jvmTarget = JAVA_VERSION.toString()
        }

        dependencies {
            add("implementation", libs.findLibrary("timber").get())
            add("implementation", libs.findLibrary("kotlin.serialization.json").get())
        }
    }
}
