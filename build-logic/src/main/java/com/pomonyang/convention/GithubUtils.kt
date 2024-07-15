package com.pomonyang.convention

import java.io.File
import java.util.concurrent.TimeUnit

object GithubUtils {
    fun commitHash(): String = "git rev-parse --short=8 HEAD".runCommand().trim()
    fun lastCommitMessage(): String = "git show -s --format=%B".runCommand().trim()

    private fun String.runCommand(workingDir: File = File(".")): String {
        return try {
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}