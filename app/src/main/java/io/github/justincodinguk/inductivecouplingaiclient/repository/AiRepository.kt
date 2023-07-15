package io.github.justincodinguk.inductivecouplingaiclient.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.justincodinguk.inductivecouplingaiclient.model.Answer
import io.github.justincodinguk.inductivecouplingaiclient.model.Question
import io.github.justincodinguk.inductivecouplingaiclient.network.AiApiService

class AiRepository(url: String) {

    private val apiService = AiApiService(url)

    suspend fun questionModel(question: Question) : Answer {
        val response = apiService.service.postQuestion(question)
        return if(response.isSuccessful) {
            response.body() ?: Answer("I couldn't get you")
        } else {
            Answer("Facing network issues")
        }
    }

}