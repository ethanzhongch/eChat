package com.ethan.easy.api

import com.ethan.easy.data.database.MessageEntity

interface LLMService {
    suspend fun generateResponse(messages: List<MessageEntity>): String
}

class LLMException(message: String) : Exception(message)
