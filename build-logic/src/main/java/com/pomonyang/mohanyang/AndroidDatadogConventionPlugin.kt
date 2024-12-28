import com.pomonyang.mohanyang.convention.findPluginId
import com.pomonyang.mohanyang.convention.implementation
import com.pomonyang.mohanyang.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidDatadogConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("datadog"))
            }

            dependencies {
                implementation(libs.findLibrary("datadog-rum"))
                implementation(libs.findLibrary("datadog-compose"))
                implementation(libs.findLibrary("datadog-okhttp"))
            }
        }
    }
}
