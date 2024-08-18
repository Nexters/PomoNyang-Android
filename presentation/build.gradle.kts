plugins {
    id("mohanyang.android.library")
    id("mohanyang.android.hilt")
    id("mohanyang.android.library.compose")
}

android {
    namespace = "com.mohanyang.presentation"
}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger.hilt.android)
    implementation(libs.bundles.androidx.compose.navigation)
    implementation(libs.permission)
    implementation(libs.rive)

    // module impl
    implementation(projects.domain)
    implementation(projects.data)
}
