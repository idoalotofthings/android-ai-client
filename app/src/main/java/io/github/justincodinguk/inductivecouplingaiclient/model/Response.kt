package io.github.justincodinguk.inductivecouplingaiclient.model

import kotlinx.serialization.Serializable

@Serializable
sealed class JsonResponse

@Serializable
data class Question(val question: String) : JsonResponse()

@Serializable
data class Answer(val answer: String) : JsonResponse()