package com.pomonyang.mohanyang

import groovy.json.JsonSlurper
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project

open class AppVersionExtension(val name: String, val code: Int)

data class AppVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val code: Int
) {
    fun getName() = "$major.$minor.$patch"
    fun getVersionCode() = code
}

class AppVersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val versionFile = getAppVersionFile(project)
        val appVersion = getAppVersion(versionFile)
        setupAppVersionExtension(project, appVersion)
    }

    private fun getAppVersionFile(project: Project): File {
        val file = File(project.rootDir, "app-version.json")
        if (!file.exists()) {
            throw IllegalStateException("app-version.json file not found")
        }
        return file
    }

    private fun getAppVersion(versionFile: File): AppVersion {
        val appVersionJson = JsonSlurper().parseText(versionFile.readText()) as Map<*, *>
        return AppVersion(
            (appVersionJson["major"] as Number).toInt(),
            (appVersionJson["minor"] as Number).toInt(),
            (appVersionJson["patch"] as Number).toInt(),
            (appVersionJson["code"] as Number).toInt()
        )
    }

    private fun setupAppVersionExtension(project: Project, appVersion: AppVersion) {
        project.extensions.create(
            "appVersion",
            AppVersionExtension::class.java,
            appVersion.getName(),
            appVersion.getVersionCode()
        )
    }
}
