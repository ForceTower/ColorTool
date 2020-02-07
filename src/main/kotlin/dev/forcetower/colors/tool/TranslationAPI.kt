package dev.forcetower.colors.tool

import dev.forcetower.colors.tool.service.TranslationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TranslationAPI {
    private val service = Retrofit.Builder()
        .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TranslationService::class.java)


    fun translate(text: String, from: String, to: String): String {
        val lang = "$from-$to"
        return service.translate(text, lang).execute().body()!!.first()
    }
}