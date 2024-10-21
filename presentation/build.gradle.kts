plugins {
    id("mohanyang.android.library")
    id("mohanyang.android.hilt")
    id("mohanyang.android.library.compose")
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.mohanyang.presentation"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger.hilt.android)
    implementation(libs.bundles.androidx.compose.navigation)
    implementation(libs.permission)
    implementation(libs.rive)
    implementation(libs.lottie.compose)

    // module impl
    implementation(projects.domain)
    implementation(projects.data)

    screenshotTestImplementation(libs.androidx.compose.tooling)
}
