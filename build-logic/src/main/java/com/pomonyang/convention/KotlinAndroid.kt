package com.pomonyang.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = ProjectConfigurations.COMPILE_SDK

        defaultConfig {
            minSdk = ProjectConfigurations.MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            resourceConfigurations.addAll(listOf("en", "ko"))
        }

        compileOptions {
            sourceCompatibility = ProjectConfigurations.JAVA_VERSION
            targetCompatibility = ProjectConfigurations.JAVA_VERSION
        }

        buildFeatures {
            buildConfig = true
        }

        kotlinOptions {
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )

            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        dependencies {
            add("implementation", libs.findLibrary("timber").get())
            add("implementation", libs.findLibrary("kotlin.serialization.json").get())
        }
    }

    configureKotlin()
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
}
