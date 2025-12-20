package com.ethan.easy.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethan.easy.data.repository.ChatRepository
import com.ethan.easy.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ChatEffect {
    data class ShowSnackbar(val message: String) : ChatEffect
}

class ChatViewModel(
    private val repository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    init {
        // Observe messages and settings together to compute isKeyMissing
        viewModelScope.launch {
            combine(
                repository.messages,
                repository.sessions,
                repository.activeSessionId,
                settingsRepository.settings,
                _uiState
            ) { messages, sessions, activeSessionId, settings, currentState ->
                val chatMessages = messages.map { entity ->
                    ChatMessage(
                        id = entity.id,
                        content = entity.content,
                        isUser = entity.role == "user",
                        role = entity.role,
                        timestamp = entity.timestamp
                    )
                }
                
                val currentModel = currentState.selectedModel
                val keyMissing = when (currentModel) {
                    ChatModel.OpenAi -> settings.openaiKey.isBlank()
                    ChatModel.DeepSeek -> settings.deepseekKey.isBlank()
                    ChatModel.Gemini -> settings.geminiKey.isBlank()
                }

                currentState.copy(
                    messages = chatMessages,
                    sessions = sessions,
                    selectedSessionId = activeSessionId,
                    isKeyMissing = keyMissing
                )
            }.collect { updatedState ->
                _uiState.update { updatedState }
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
            is ChatIntent.ClearError -> {
                // No-op for now
            }
        }
    }

    private fun sendMessage() {
        if (_uiState.value.isKeyMissing) {
            viewModelScope.launch {
                _effect.emit(ChatEffect.ShowSnackbar("Please configure ${ _uiState.value.selectedModel.displayName } API Key in Settings"))
            }
            return
        }

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
