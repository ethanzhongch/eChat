package com.ethan.easy.api.deepseek

import kotlinx.serialization.Serializable

@Serializable
data class DeepSeekRequest(
    val model: String,
    val messages: List<DeepSeekMessage>,
    val stream: Boolean = false
)

@Serializable
data class DeepSeekMessage(
    val role: String,
    val content: String
)

@Serializable
data class DeepSeekResponse(
    val choices: List<DeepSeekChoice>
)

@Serializable
data class DeepSeekChoice(
    val message: DeepSeekMessage
)
