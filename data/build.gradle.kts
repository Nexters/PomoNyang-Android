plugins {
    id("pomonyang.android.library")
    id("pomonyang.android.hilt")
}

android {
    namespace = "com.pomonyang.data"
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.serialization.converter)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.androidx.datastore)
}
