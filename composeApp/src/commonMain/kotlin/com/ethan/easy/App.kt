package com.ethan.easy

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ethan.easy.ui.chat.ChatScreen
import com.ethan.easy.ui.settings.SettingsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    Chat, Settings
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.Chat) }

        when (currentScreen) {
            Screen.Chat -> ChatScreen(
                onNavigateToSettings = { currentScreen = Screen.Settings }
            )
            Screen.Settings -> SettingsScreen(
                onBack = { currentScreen = Screen.Chat }
            )
        }
    }
}