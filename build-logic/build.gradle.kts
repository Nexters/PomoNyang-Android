plugins {
    `kotlin-dsl`
}

group = "com.pomonyang.buildlogic"

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
            id = "pomonyang.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "pomonyang.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidFirebase") {
            id = "pomonyang.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidLibrary") {
            id = "pomonyang.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "pomonyang.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "pomonyang.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
