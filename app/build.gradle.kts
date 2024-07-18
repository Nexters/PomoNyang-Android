import com.pomonyang.convention.GithubUtils

plugins {
    id("pomonyang.android.application")
    id("pomonyang.android.hilt")
    id("pomonyang.android.application.compose")
    id("pomonyang.android.application.firebase")
}

android {
    namespace = "com.pomonyang"

    defaultConfig {
        applicationId = "com.pomonyang"
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
