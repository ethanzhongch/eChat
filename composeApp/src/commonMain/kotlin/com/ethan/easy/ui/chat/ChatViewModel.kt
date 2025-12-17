package com.ethan.easy.ui.chat

import com.ethan.easy.util.PlatformTime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

// import kotlinx.datetime.Clock

/**
 * ViewModel for the Chat Screen.
 * Handles business logic and state updates following MVI architecture.
 */
class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> {
                _uiState.update { it.copy(inputText = intent.text) }
            }
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.SelectModel -> {
                _uiState.update { it.copy(selectedModel = intent.model) }
            }
            is ChatIntent.CreateNewChat -> {
                _uiState.update { it.copy(messages = emptyList(), inputText = "") }
            }
        }
    }

    private fun sendMessage() {
        val currentInput = _uiState.value.inputText
        if (currentInput.isBlank()) return
        // Get the current Instant
        val userMessage = ChatMessage(
            id = Random.nextLong().toString(),
            content = currentInput,
            isUser = true,
            timestamp = PlatformTime.getCurrentTimeMillis()
        )

        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isLoading = true
            )
        }

        // Mock AI Response
        viewModelScope.launch {
            delay(1500) // Simulate network delay
            val aiMessage = ChatMessage(
                id = Random.nextLong().toString(),
                content = "This is a mock response from ${_uiState.value.selectedModel.displayName} to: \"$currentInput\"",
                isUser = false,
                timestamp = PlatformTime.getCurrentTimeMillis()
            )
            _uiState.update {
                it.copy(
                    messages = it.messages + aiMessage,
                    isLoading = false
                )
            }
        }
    }
}
