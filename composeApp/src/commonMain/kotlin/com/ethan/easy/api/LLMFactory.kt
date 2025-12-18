package com.ethan.easy.api

import com.ethan.easy.api.deepseek.DeepSeekService
import com.ethan.easy.api.gemini.GeminiService
import com.ethan.easy.api.openai.OpenAIService
import io.ktor.client.HttpClient

class LLMFactory(
    private val client: HttpClient,
    private val openaiKey: String,
    private val deepseekKey: String,
    private val geminiKey: String
) {
    fun getService(modelProvider: String): LLMService {
        return when (modelProvider) {
            "OpenAI" -> OpenAIService(client, openaiKey)
            "DeepSeek" -> DeepSeekService(client, deepseekKey)
            "Gemini" -> GeminiService(client, geminiKey)
            else -> throw IllegalArgumentException("Unknown Model Provider: $modelProvider")
        }
    }
}
