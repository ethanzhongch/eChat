package com.ethan.easy.api.deepseek

import com.ethan.easy.api.LLMException
import com.ethan.easy.api.LLMService
import com.ethan.easy.data.database.MessageEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeepSeekService(
    private val client: HttpClient,
    private val apiKey: String
) : LLMService {
    private val baseUrl = "https://api.deepseek.com"

    override suspend fun generateResponse(messages: List<MessageEntity>): String {
        val deepSeekMessages = messages.map {
            DeepSeekMessage(
                role = if (it.role == "assistant") "assistant" else "user",
                content = it.content
            )
        }
        
        val request = DeepSeekRequest(
            model = "deepseek-chat",
            messages = deepSeekMessages
        )
        
        val response: DeepSeekResponse = client.post("$baseUrl/chat/completions") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $apiKey")
            setBody(request)
        }.body()
        
        if (response.error != null) {
            throw LLMException(response.error.message ?: "Unknown DeepSeek error")
        }
        
        return response.choices?.firstOrNull()?.message?.content ?: "No response from DeepSeek"
    }
}
