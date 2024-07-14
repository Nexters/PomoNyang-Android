import com.pomonyang.convention.findPluginId
import com.pomonyang.convention.implementation
import com.pomonyang.convention.ksp
import com.pomonyang.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("hilt"))
                apply(libs.findPluginId("ksp"))
            }

            dependencies {
                implementation(libs.findLibrary("dagger-hilt-android"))
                ksp(libs.findLibrary("dagger-hilt-android-compiler"))
            }
        }
    }
}
