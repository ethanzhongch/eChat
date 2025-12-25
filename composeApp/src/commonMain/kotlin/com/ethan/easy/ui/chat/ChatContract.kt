package com.ethan.easy.ui.chat

import androidx.compose.runtime.Immutable
import com.ethan.easy.data.database.SessionEntity

/**
 * MVI State for the Chat Screen.
 *
 * @param messages List of chat messages.
 * @param sessions List of chat sessions (history).
 * @param selectedModel The currently selected LLM.
 * @param isLoading Whether the AI is currently generating a response.
 * @param inputText Current text in the input field.
 */
@Immutable
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val sessions: List<SessionEntity> = emptyList(),
    val selectedModel: ChatModel = ChatModel.DeepSeek,
    val isLoading: Boolean = false,
    val inputText: String = "",
    val isKeyMissing: Boolean = false,
    val selectedSessionId: String? = null
)

@Immutable
data class ChatMessage(
    val id: String,
    val content: String,
    val isUser: Boolean,
    val role: String, // "user", "assistant", "system"
    val timestamp: Long
)

/**
 * MVI Intents (User Actions).
 */
sealed interface ChatIntent {
    data class UpdateInput(val text: String) : ChatIntent
    data object SendMessage : ChatIntent
    data class SelectModel(val model: ChatModel) : ChatIntent
    data object CreateNewChat : ChatIntent
    data class LoadSession(val sessionId: String) : ChatIntent
    data object ClearError : ChatIntent // Helper for UX
}

/**
 * Supported LLM Providers.
 */
enum class ChatModel(val displayName: String, val iconRes: String? = null) {
    Gemini("Gemini"),
    DeepSeek("DeepSeek"),
    OpenAi("ChatGPT")
}
