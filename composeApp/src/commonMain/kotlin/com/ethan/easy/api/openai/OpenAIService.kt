package com.ethan.easy.api.openai

import com.ethan.easy.api.LLMService
import com.ethan.easy.data.database.MessageEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OpenAIService(
    private val client: HttpClient,
    private val apiKey: String
) : LLMService {
    private val baseUrl = "https://api.openai.com/v1"

    override suspend fun generateResponse(messages: List<MessageEntity>): String {
        val openAIMessages = messages.map {
            OpenAIMessage(
                role = if (it.role == "assistant") "assistant" else "user",
                content = it.content
            )
        }
        
        val request = OpenAIRequest(
            model = "gpt-4o",
            messages = openAIMessages
        )
        
        val response: OpenAIResponse = client.post("$baseUrl/chat/completions") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $apiKey")
            setBody(request)
        }.body()
        
        return response.choices.firstOrNull()?.message?.content ?: "No response from OpenAI"
    }
}
