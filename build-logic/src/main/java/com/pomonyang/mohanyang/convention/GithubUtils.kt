package com.pomonyang.mohanyang.convention

import java.io.File
import java.util.concurrent.TimeUnit

object GithubUtils {
    fun commitHash(): String = runCommand("git rev-parse --short=8 HEAD")

    fun lastCommitMessage(): String = runCommand("git show -s --format=%B")

    private fun runCommand(
        command: String,
        workingDir: File = File("."),
    ): String = try {
        val parts = command.split("\\s".toRegex())
        val process =
            ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        process.waitFor(60, TimeUnit.MINUTES)
        process.inputStream
            .bufferedReader()
            .readText()
            .trim()
    } catch (e: Exception) {
        e.printStackTrace()
        "Command failed"
    }
}
