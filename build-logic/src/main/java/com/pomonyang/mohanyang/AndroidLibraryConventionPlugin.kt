import com.android.build.gradle.LibraryExtension
import com.pomonyang.mohanyang.convention.configureKotlinAndroid
import com.pomonyang.mohanyang.convention.configureSecret
import com.pomonyang.mohanyang.convention.findPluginId
import com.pomonyang.mohanyang.convention.libs
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
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureSecret()
            }
        }
    }
}
