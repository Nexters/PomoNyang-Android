package com.pomonyang.ui

import timber.log.Timber

internal class DebugTimberTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String = "${super.createStackElementTag(element)}"

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val element = Throwable().stackTrace.first { stackElement ->
            stackElement.className.run {
                startsWith("timber.log.").not() && contains("Timber").not()
            }
        }
        val prefix = "[${element.fileName}:${element.lineNumber}]"
        super.log(priority, tag, "$prefix$message", t)
    }
}