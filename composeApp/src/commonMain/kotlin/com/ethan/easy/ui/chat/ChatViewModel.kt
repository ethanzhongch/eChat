package com.ethan.easy.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethan.easy.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Observe messages from repository
        viewModelScope.launch {
            repository.messages.collect { entities ->
                val chatMessages = entities.map { entity ->
                    ChatMessage(
                        id = entity.id,
                        content = entity.content,
                        isUser = entity.role == "user",
                        timestamp = entity.timestamp
                    )
                }
                _uiState.update { it.copy(messages = chatMessages) }
            }
        }

        // Observe sessions for history
        viewModelScope.launch {
            repository.sessions.collect { sessions ->
                 _uiState.update { it.copy(sessions = sessions) }
            }
        }
    }

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
                repository.newChat()
                _uiState.update { it.copy(inputText = "") }
            }
            is ChatIntent.LoadSession -> {
                repository.loadSession(intent.sessionId)
                
                // Restore model from session if possible
                val session = _uiState.value.sessions.find { it.id == intent.sessionId }
                session?.let { s ->
                   val model = ChatModel.entries.find { it.name == s.modelProvider }
                   if (model != null) {
                       _uiState.update { it.copy(selectedModel = model) }
                   }
                }
            }
        }
    }

    private fun sendMessage() {
        val currentInput = _uiState.value.inputText
        if (currentInput.isBlank()) return

        val currentModel = _uiState.value.selectedModel

        _uiState.update { it.copy(inputText = "", isLoading = true) }

        viewModelScope.launch {
            repository.sendMessage(currentInput, currentModel)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
