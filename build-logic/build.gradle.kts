plugins {
    `kotlin-dsl`
}

group = "com.mohanyang.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.compiler.extension)
    compileOnly(libs.gradle.secrets)
    compileOnly(libs.firebase.perf.gradle)
    compileOnly(libs.firebase.crashlytics.gradle)
    compileOnly(libs.firebase.appdistribution.gradle)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mohanyang.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "mohanyang.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidFirebase") {
            id = "mohanyang.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidLibrary") {
            id = "mohanyang.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mohanyang.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "mohanyang.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
