package dev.forcetower.colors.tool

import kotlin.system.exitProcess

fun main() {
    val colors = Extractor().execute()
    println(colors.size)
    ThemeXMLWriter.write(colors, true)
    exitProcess(0)
}