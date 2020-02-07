package dev.forcetower.colors.tool

import dev.forcetower.colors.tool.model.ColorMap
import java.io.File

object ThemeXMLWriter {
    private const val themeNamePrefix = "ThemeOverlay.PrimaryPalette"
    private const val arrayName = "material_primary_palettes"
    private const val stringArraysName = "material_palettes_content_description"
    private const val stringNameKeyPrefix = "generated_material_"

    fun write(colors: List<ColorMap>, translate: Boolean = true, lang: String = "pt") {
        theme(colors)
        array(colors)
        string(colors)
        if (translate) stringLang(lang, colors)
        stringArray(colors)
    }

    private fun theme(colors: List<ColorMap>) {
        val theme = File("theme.xml")
        val builder = StringBuilder("<resources>")
        colors.forEach {
            builder.append("\n    <style name=\"${themeNamePrefix}.${it.name}\" parent=\"\">\n")
            it.map.entries.forEach { entry ->
                builder.append("        <item name=\"${entry.key}\">${entry.value}</item>\n")
            }
            builder.append("    </style>\n")
        }
        builder.append("</resources>\n")
        theme.writeText(builder.toString())
    }

    private fun array(colors: List<ColorMap>) {
        val array = File("array.xml")
        val builder = StringBuilder("<array name=\"${arrayName}\">\n")
        colors.forEach {
            builder.append("    <item>@style/${themeNamePrefix}.${it.name}</item>\n")
        }
        builder.append("</array>")
        array.writeText(builder.toString())
    }

    private fun string(colors: List<ColorMap>) {
        val strings = File("strings_en.xml")
        val builder = StringBuilder("\n<!-- Generated Colors -->\n\n")
        colors.forEach { color ->
            val name = color.unfilteredName.split(" ").joinToString("_") { it.decapitalize() }
            builder.append("<string name=\"${stringNameKeyPrefix}${name}\">${color.unfilteredName}</string>\n")
        }
        builder.append("\n<!-- End of generated colors -->")
        strings.writeText(builder.toString())
    }

    private fun stringLang(lang: String, colors: List<ColorMap>) {
        val strings = File("strings_${lang}.xml")
        val builder = StringBuilder("\n<!-- Generated Colors -->\n\n")
        colors.forEach { color ->
            val name = color.unfilteredName.split(" ").joinToString("_") { it.decapitalize() }
            builder.append("<string name=\"${stringNameKeyPrefix}${name}\">${TranslationAPI.translate(color.unfilteredName, "en", lang)}</string>\n")
        }
        builder.append("\n<!-- End of generated colors -->")
        strings.writeText(builder.toString())
    }


    private fun stringArray(colors: List<ColorMap>) {
        val array = File("string_array.xml")
        val builder = StringBuilder("<array name=\"$stringArraysName\">\n")
        colors.forEach { color ->
            val name = color.unfilteredName.split(" ").joinToString("_") { it.decapitalize() }
            builder.append("    <item>@string/${stringNameKeyPrefix}${name}</item>\n")
        }
        builder.append("</array>")
        array.writeText(builder.toString())
    }
}