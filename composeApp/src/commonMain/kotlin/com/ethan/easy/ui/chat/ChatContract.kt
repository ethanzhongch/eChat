package com.ethan.easy.ui.chat

import androidx.compose.runtime.Immutable

/**
 * MVI State for the Chat Screen.
 *
 * @param messages List of chat messages.
 * @param selectedModel The currently selected LLM.
 * @param isLoading Whether the AI is currently generating a response.
 * @param inputText Current text in the input field.
 */
@Immutable
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val selectedModel: ChatModel = ChatModel.DeepSeek,
    val isLoading: Boolean = false,
    val inputText: String = ""
)

@Immutable
data class ChatMessage(
    val id: String,
    val content: String,
    val isUser: Boolean,
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
}

/**
 * Supported LLM Providers.
 */
enum class ChatModel(val displayName: String, val iconRes: String? = null) {
    Gemini("Gemini"),
    DeepSeek("DeepSeek"),
    OpenAi("ChatGPT 4")
}
