import com.android.build.gradle.LibraryExtension
import com.pomonyang.convention.configureKotlinAndroid
import com.pomonyang.convention.configureSecret
import com.pomonyang.convention.findPluginId
import com.pomonyang.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("android.library"))
                apply(libs.findPluginId("kotlin.android"))
                apply(libs.findPluginId("kotlin.serialization"))
                apply(libs.findPluginId("gradle.secrets"))
                apply(libs.findPluginId("ktlint"))
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureSecret()
            }
        }
    }
}
