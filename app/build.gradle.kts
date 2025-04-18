import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.loadPropertiesFile
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import com.pomonyang.mohanyang.convention.GithubUtils

plugins {
    id("mohanyang.android.application")
    id("mohanyang.android.hilt")
    id("mohanyang.android.application.compose")
    id("mohanyang.android.application.firebase")
    id("mohanyang.android.datadog")
    id("mohanyang.appversion")
}

android {
    namespace = "com.pomonyang.mohanyang"

    defaultConfig {
        applicationId = "com.pomonyang.mohanyang"
        versionCode = appVersion.code
        versionName = appVersion.name
    }

    signingConfigs {
        create("release") {
            val props = loadPropertiesFile("../key.secrets.properties")
            storeFile = file(props.getProperty("STORE_FILE_PATH"))
            storePassword = props.getProperty("STORE_PASSWORD")
            keyAlias = props.getProperty("KEY_ALIAS")
            keyPassword = props.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-project.txt",
            )
            signingConfig = signingConfigs.getByName("release")
            firebaseAppDistribution {
                releaseNotes = "[${GithubUtils.commitHash()}] - ${GithubUtils.lastCommitMessage()}"
                groups = "뽀모냥"
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            aaptOptions.cruncherEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-project.txt",
            )
            firebaseAppDistribution {
                releaseNotes = "[${GithubUtils.commitHash()}]-${GithubUtils.lastCommitMessage()}"
                groups = "뽀모냥"
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.bundles.androidx.compose.navigation)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.firebase.messaging)
    implementation(libs.rive)
    implementation(libs.startup)
    // module impl
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)
}
