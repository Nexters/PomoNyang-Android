plugins {
    id("mohanyang.android.library")
    id("mohanyang.android.hilt")
}

android {
    namespace = "com.mohanyang.data"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.serialization.converter)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.datadog.okhttp)
    ksp(libs.androidx.room.compiler)
}
