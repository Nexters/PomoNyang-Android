plugins {
    id("pomonyang.android.application")
    id("pomonyang.android.hilt")
    id("pomonyang.android.application.compose")
}

android {
    namespace = "com.pomonyang"

    defaultConfig {
        applicationId = "com.pomonyang"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-project.txt"
            )
            // TODO Distribution
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            aaptOptions.cruncherEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-project.txt"
            )

            // TODO Distribution
        }
    }
}

dependencies {

    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.timber)

    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.animation)
}