package com.ethan.easy.api.gemini

import com.ethan.easy.api.LLMException
import com.ethan.easy.api.LLMService
import com.ethan.easy.data.database.MessageEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GeminiService(
    private val client: HttpClient,
    private val apiKey: String
) : LLMService {
    override suspend fun generateResponse(messages: List<MessageEntity>): String {
        val geminiContents = messages.map {
            GeminiContent(
                role = if (it.role == "user") "user" else "model",
                parts = listOf(GeminiPart(text = it.content))
            )
        }
        
        val request = GeminiRequest(
            contents = geminiContents
        )
        
        val response: GeminiResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        
        // Handle API Error field
        if (response.error != null) {
            throw LLMException(
                response.error.message ?: response.error.status ?: "Unknown Gemini error"
            )
        }

        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response from Gemini"
    }
}
