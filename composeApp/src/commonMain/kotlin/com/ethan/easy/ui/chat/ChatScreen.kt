package com.ethan.easy.ui.chat

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.ethan.easy.ui.chat.components.ChatDrawer
import com.ethan.easy.ui.chat.components.ChatInput
import com.ethan.easy.ui.chat.components.ChatTopBar
import com.ethan.easy.ui.chat.components.EmptyChatView
import com.ethan.easy.ui.chat.components.MessageListView
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: ChatViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    // Handle ViewModel effects (Snackbar)
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ChatEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatDrawer(
                sessions = uiState.sessions,
                onNewChat = {
                    viewModel.handleIntent(ChatIntent.CreateNewChat)
                    scope.launch { drawerState.close() }
                },
                onSessionSelected = { sessionId ->
                    viewModel.handleIntent(ChatIntent.LoadSession(sessionId))
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ChatTopBar(
                    title = if (uiState.messages.isEmpty()) "New chat" else uiState.selectedModel.displayName,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChatClick = { viewModel.handleIntent(ChatIntent.CreateNewChat) },
                    onSettingsClick = onNavigateToSettings
                )
            },
            bottomBar = {
                ChatInput(
                    text = uiState.inputText,
                    onTextChanged = { viewModel.handleIntent(ChatIntent.UpdateInput(it)) },
                    onSendClick = { viewModel.handleIntent(ChatIntent.SendMessage) },
                    isLoading = uiState.isLoading
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // State Switching: Empty vs Active
                if (uiState.messages.isEmpty()) {
                    EmptyChatView(
                        selectedModel = uiState.selectedModel,
                        onModelSelected = { viewModel.handleIntent(ChatIntent.SelectModel(it)) },
                        isKeyMissing = uiState.isKeyMissing,
                        onNavigateToSettings = onNavigateToSettings
                    )
                } else {
                    MessageListView(
                        messages = uiState.messages,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
