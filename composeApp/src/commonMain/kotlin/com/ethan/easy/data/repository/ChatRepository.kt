package com.ethan.easy.data.repository

import com.ethan.easy.api.LLMException
import com.ethan.easy.data.database.MessageDao
import com.ethan.easy.data.database.MessageEntity
import com.ethan.easy.data.database.SessionDao
import com.ethan.easy.data.database.SessionEntity
import com.ethan.easy.ui.chat.ChatModel
import com.ethan.easy.util.PlatformTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlin.random.Random

import com.ethan.easy.api.LLMFactory
import kotlinx.coroutines.flow.first

class ChatRepository(
    private val sessionDao: SessionDao,
    private val messageDao: MessageDao,
    private val llmFactory: LLMFactory
) {
    private val _activeSessionId = MutableStateFlow<String?>(null)
    val activeSessionId = _activeSessionId.asStateFlow()

    // Expose all sessions as history
    val sessions: Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    // Expose messages for the active session, or empty if none
    val messages: Flow<List<MessageEntity>> = _activeSessionId.flatMapLatest { sessionId ->
        if (sessionId == null) flowOf(emptyList()) else messageDao.getMessagesForSession(sessionId)
    }

    fun loadSession(sessionId: String) {
        _activeSessionId.value = sessionId
    }

    fun newChat() {
        _activeSessionId.value = null
    }

    fun getHistory(): Flow<List<SessionEntity>> = sessions

    suspend fun sendMessage(text: String, currentModel: ChatModel) = withContext(Dispatchers.IO) {
        var sessionId = _activeSessionId.value

        // If no active session, create one
        if (sessionId == null) {
            val newId = Random.nextLong().toString() // Simple ID generation
            val newSession = SessionEntity(
                id = newId,
                title = text.take(20).trim() + "...",
                createdAt = PlatformTime.getCurrentTimeMillis(),
                modelProvider = currentModel.name
            )
            sessionDao.insert(newSession)
            sessionId = newId
            _activeSessionId.value = sessionId
        } 
        
        // Use non-null sessionId (we either had it or just created it)
        val currentSessionId = sessionId!!

        // Insert User Message
        val userMsg = MessageEntity(
            id = Random.nextLong().toString(),
            sessionId = currentSessionId,
            role = "user",
            content = text,
            timestamp = PlatformTime.getCurrentTimeMillis(),
            status = "Sent",
            modelProvider = currentModel.name
        )
        messageDao.insert(userMsg)

        // Real AI Response
        try {
            // Fetch relevant context (history) for the API call - EXCLUDE system/error messages
            val allMessages = messageDao.getMessagesForSession(currentSessionId).first()
            val history = allMessages.filter { it.role == "user" || it.role == "assistant" }
            
            val service = llmFactory.getService(currentModel.name)
            val responseText = service.generateResponse(history)

            val aiMsg = MessageEntity(
                id = Random.nextLong().toString(),
                sessionId = currentSessionId,
                role = "assistant",
                content = responseText,
                timestamp = PlatformTime.getCurrentTimeMillis(),
                status = "Sent",
                modelProvider = currentModel.name
            )
            messageDao.insert(aiMsg)
        } catch (e: Exception) {
            println("Error in sendMessage: ${e.message}")
            // Display a safe, non-raw message to avoid leaking API keys often returned in error bodies
            // TODO: Gemini's response may contain an API key; do not directly display the error message to the user.
            val safeContent = if (e is LLMException) {
                "The AI service returned an error."
            } else {
                "A network error occurred. Please check your internet connection."
            }
            
            val errorMsg = MessageEntity(
                id = Random.nextLong().toString(),
                sessionId = currentSessionId,
                role = "system",
                content = safeContent,
                timestamp = PlatformTime.getCurrentTimeMillis(),
                status = "Error",
                modelProvider = currentModel.name
            )
            messageDao.insert(errorMsg)
        }
    }
}
