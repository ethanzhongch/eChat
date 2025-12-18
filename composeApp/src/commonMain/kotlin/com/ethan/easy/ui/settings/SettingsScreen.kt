package com.ethan.easy.ui.settings

import androidx.compose.material3.Button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinInject()
) {
    val settings by viewModel.uiState.collectAsState()

    var openaiVisible by rememberSaveable { mutableStateOf(false) }
    var deepseekVisible by rememberSaveable { mutableStateOf(false) }
    var geminiVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Configure API Keys",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Your keys are stored locally on your device using Jetpack DataStore.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OpenAI Field
            ApiKeyField(
                label = "OpenAI API Key",
                value = settings.openaiKey,
                isVisible = openaiVisible,
                onValueChange = { viewModel.updateKey("OpenAI", it) },
                onToggleVisibility = { openaiVisible = !openaiVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DeepSeek Field
            ApiKeyField(
                label = "DeepSeek API Key",
                value = settings.deepseekKey,
                isVisible = deepseekVisible,
                onValueChange = { viewModel.updateKey("DeepSeek", it) },
                onToggleVisibility = { deepseekVisible = !deepseekVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gemini Field

            ApiKeyField(
                label = "Gemini API Key",
                value = settings.geminiKey,
                isVisible = geminiVisible,
                onValueChange = { viewModel.updateKey("Gemini", it) },
                onToggleVisibility = { geminiVisible = !geminiVisible }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save & Close")
            }
        }
    }
}

@Composable
private fun ApiKeyField(
    label: String,
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) "Hide" else "Show"
                )
            }
        },
        singleLine = true
    )
}
