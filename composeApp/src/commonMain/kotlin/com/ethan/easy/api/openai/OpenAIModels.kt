package com.ethan.easy.api.openai

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    val model: String,
    val messages: List<OpenAIMessage>,
    val stream: Boolean = false
)

@Serializable
data class OpenAIMessage(
    val role: String,
    val content: String
)

@Serializable
data class OpenAIResponse(
    val choices: List<OpenAIChoice>? = null,
    val error: OpenAIError? = null
)

@Serializable
data class OpenAIError(
    val message: String? = null,
    val type: String? = null,
    val code: String? = null
)

@Serializable
data class OpenAIChoice(
    val message: OpenAIMessage? = null
)
