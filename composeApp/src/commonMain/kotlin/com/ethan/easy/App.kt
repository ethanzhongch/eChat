package com.ethan.easy

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.ethan.easy.ui.chat.ChatScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        ChatScreen()
    }
}