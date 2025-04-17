plugins {
    id("mohanyang.android.library")
    id("mohanyang.android.hilt")
    id("mohanyang.android.library.compose")
    id("mohanyang.appversion")
}

android {
    namespace = "com.mohanyang.presentation"
    defaultConfig {
        buildConfigField("String", "APP_VERSION", "\"${appVersion.name}\"")
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger.hilt.android)
    implementation(libs.bundles.androidx.compose.navigation)
    implementation(libs.permission)
    implementation(libs.rive)
    implementation(libs.lottie.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // module impl
    implementation(projects.domain)
    implementation(projects.data)
}
