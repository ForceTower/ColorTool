package dev.forcetower.colors.tool.service

import dev.forcetower.colors.tool.Constants
import dev.forcetower.colors.tool.service.dto.Translation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationService {
    @GET("translate")
    fun translate(
        @Query("text") text: String,
        @Query("lang") lang: String,
        @Query("key") key: String = Constants.TRANSLATION_KEY
    ): Call<Translation>
}