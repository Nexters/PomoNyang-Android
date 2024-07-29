import com.pomonyang.mohanyang.convention.GithubUtils

plugins {
    id("mohanyang.android.application")
    id("mohanyang.android.hilt")
    id("mohanyang.android.application.compose")
    id("mohanyang.android.application.firebase")
}

android {
    namespace = "com.pomonyang.mohanyang"

    defaultConfig {
        applicationId = "com.pomonyang.mohanyang"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-project.txt"
            )
            firebaseAppDistribution {
                releaseNotes = "[${GithubUtils.commitHash()}]-${GithubUtils.lastCommitMessage()}"
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
                "proguard-project.txt"
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

    // module impl
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)
}
