plugins {
    id("pomonyang.android.library")
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.pomonyang.domain"
}

dependencies {
    implementation(projects.data)
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.javax.inject)
}
