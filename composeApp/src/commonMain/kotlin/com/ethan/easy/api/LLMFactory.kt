package com.ethan.easy.api

import com.ethan.easy.api.deepseek.DeepSeekService
import com.ethan.easy.api.gemini.GeminiService
import com.ethan.easy.api.openai.OpenAIService
import io.ktor.client.HttpClient

import com.ethan.easy.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class LLMFactory(
    private val client: HttpClient,
    private val settingsRepository: SettingsRepository
) {
    suspend fun getService(modelProvider: String): LLMService {
        val settings = settingsRepository.settings.first()
        
        return when (modelProvider) {
            "OpenAI" -> OpenAIService(client, settings.openaiKey)
            "DeepSeek" -> DeepSeekService(client, settings.deepseekKey)
            "Gemini" -> GeminiService(client, settings.geminiKey)
            else -> throw IllegalArgumentException("Unknown Model Provider: $modelProvider")
        }
    }
}
