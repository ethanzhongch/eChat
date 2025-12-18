package com.ethan.easy.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserSettings(
    val openaiKey: String = "",
    val deepseekKey: String = "",
    val geminiKey: String = ""
)

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val OPENAI_KEY = stringPreferencesKey("openai_api_key")
        val DEEPSEEK_KEY = stringPreferencesKey("deepseek_api_key")
        val GEMINI_KEY = stringPreferencesKey("gemini_api_key")
    }

    val settings: Flow<UserSettings> = dataStore.data.map { preferences ->
        UserSettings(
            openaiKey = preferences[PreferencesKeys.OPENAI_KEY] ?: "",
            deepseekKey = preferences[PreferencesKeys.DEEPSEEK_KEY] ?: "",
            geminiKey = preferences[PreferencesKeys.GEMINI_KEY] ?: ""
        )
    }

    suspend fun saveApiKey(provider: String, key: String) {
        dataStore.edit { preferences ->
            when (provider) {
                "OpenAI" -> preferences[PreferencesKeys.OPENAI_KEY] = key
                "DeepSeek" -> preferences[PreferencesKeys.DEEPSEEK_KEY] = key
                "Gemini" -> preferences[PreferencesKeys.GEMINI_KEY] = key
            }
        }
    }
}
