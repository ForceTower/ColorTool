package dev.forcetower.colors.tool.service.dto

data class Translation (
    val text: List<String>
) {
    fun first() = text.first()
}