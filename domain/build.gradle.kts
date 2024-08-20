plugins {
    id("mohanyang.android.library")
}

android {
    namespace = "com.mohanyang.domain"
}

dependencies {
    implementation(projects.data)
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.javax.inject)
    implementation(libs.androidx.compose.runtime)
}
