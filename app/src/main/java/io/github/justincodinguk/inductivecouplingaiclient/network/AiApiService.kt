package io.github.justincodinguk.inductivecouplingaiclient.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.justincodinguk.inductivecouplingaiclient.model.Answer
import io.github.justincodinguk.inductivecouplingaiclient.model.Question
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApi {

    @POST("ai")
    suspend fun postQuestion(@Body question: Question): Response<Answer>

}

class AiApiService(url: String) {

    private val loggingInterceptor = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofitClient = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    val service: AiApi = retrofitClient.create(AiApi::class.java)
}