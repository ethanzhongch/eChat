package com.ethan.easy.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethan.easy.data.repository.SettingsRepository
import com.ethan.easy.data.repository.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserSettings())
    val uiState: StateFlow<UserSettings> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settings.collect { settings ->
                _uiState.update { settings }
            }
        }
    }

    fun updateKey(provider: String, key: String) {
        viewModelScope.launch {
            repository.saveApiKey(provider, key)
        }
    }
}
