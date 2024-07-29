import com.android.build.api.dsl.ApplicationExtension
import com.pomonyang.mohanyang.convention.ProjectConfigurations
import com.pomonyang.mohanyang.convention.configureKotlinAndroid
import com.pomonyang.mohanyang.convention.configureSecret
import com.pomonyang.mohanyang.convention.findPluginId
import com.pomonyang.mohanyang.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("android.application"))
                apply(libs.findPluginId("kotlin.android"))
                apply(libs.findPluginId("kotlin.serialization"))
                apply(libs.findPluginId("gradle.secrets"))
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureSecret()
                defaultConfig.targetSdk = ProjectConfigurations.TARGET_SDK

                packaging {
                    resources {
                        excludes.add("/META-INF/{AL2.0,LGPL2.1}")
                    }
                }
            }
        }
    }
}
