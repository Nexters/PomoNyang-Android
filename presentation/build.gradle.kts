plugins {
    id("pomonyang.android.library")
    id("pomonyang.android.hilt")
    id("pomonyang.android.library.compose")
}

android {
    namespace = "com.pomonyang.presentation"
}

dependencies {
    implementation(libs.material)
    implementation(libs.dagger.hilt.android)
    implementation(libs.bundles.androidx.compose.navigation)

    // module impl
    implementation(projects.domain)
    implementation(projects.data)
}