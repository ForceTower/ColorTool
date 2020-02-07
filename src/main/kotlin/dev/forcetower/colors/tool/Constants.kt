package dev.forcetower.colors.tool

object Constants {
    val TRANSLATION_KEY: String
    get() = System.getenv("TRANSLATION_API_KEY")
        ?: throw IllegalStateException("API Key not set Read below.\nYou may not use the translation service and remove this throw " +
                "or got to: https://tech.yandex.com/translate/doc/dg/reference/translate-docpage and get a API key for you")
}