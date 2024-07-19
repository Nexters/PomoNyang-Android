plugins {
    id("pomonyang.android.library")
}

android {
    namespace = "com.pomonyang.domain"
}

dependencies {
    implementation(projects.data)
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.javax.inject)
}
