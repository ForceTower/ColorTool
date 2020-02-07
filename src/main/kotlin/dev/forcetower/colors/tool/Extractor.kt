package dev.forcetower.colors.tool

import dev.forcetower.colors.tool.model.ColorMap
import dev.forcetower.colors.tool.model.ColorSelectConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.closeQuietly
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Extractor {
    private val client = OkHttpClient.Builder().build()

    private val titles: List<String>? = null

    private val select = mapOf(
        "colorPrimary" to ColorSelectConfig(500),
        "colorPrimaryDark" to ColorSelectConfig(700),
        "colorPrimaryLight" to ColorSelectConfig(200),
        "colorPrimaryAlpha" to ColorSelectConfig(500, "19")
    )

    fun execute(): List<ColorMap> {
        val document = network()
        return process(document)
    }

    private fun process(document: Document): List<ColorMap> {
        val header = document.selectFirst("h1:contains(2014 Material Design color palettes)").parent().parent()
        val moduleModule = header.select("div[class=\"col-list\"]")[1].children()

        val modules = moduleModule.filterIndexed { index, _ -> index % 2 == 0 }.flatMap { it.children() }
        println(modules.size)
        return modules
            // follows pattern (line with content -> empty line) (last line is missing elements)
            .filter { element -> element.children().size > 2 }
            .map { module ->
                val children = module.children()
                val first = children[0]
                val text = first.text().split(" ").dropLast(2)
                val name = text.joinToString("") { it.capitalize() }
                val unfilteredName = text.joinToString(" ")
                val map = children.mapNotNull { element ->
                    val number = element.selectFirst("span[class=\"shade\"]").text().split(" ").last().toIntOrNull()
                    val hex = element.selectFirst("span[class=\"hex\"]").text()
                    val selection = select.entries.filter { it.value.code == number }.map { entry ->
                        entry.key to "#${entry.value.alpha}${hex.drop(1)}"
                    }
                    if (selection.isEmpty()) null
                    else selection
                }.flatten().groupBy ({ it.first }, { it.second }).mapValues { it.value.first() }
                ColorMap(name, unfilteredName, map)
            }.filter { titles == null || titles.contains(it.name) }
    }

    private fun network(): Document {
        val request = Request.Builder()
            .url("https://material.io/design/color/the-color-system.html")
            .build()
        val call = client.newCall(request)
        val response = call.execute()
        val body = response.peekBody(Long.MAX_VALUE)
        val string = body.string()
        body.closeQuietly()
        return Jsoup.parse(string)
    }
}