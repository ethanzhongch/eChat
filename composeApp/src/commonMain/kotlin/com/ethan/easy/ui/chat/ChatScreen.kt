package com.ethan.easy.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.ethan.easy.ui.chat.components.*
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
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
                selectedSessionId = uiState.selectedSessionId,
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
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                // Main Content
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

                // Bottom Overlay: Gradient + Floating Input
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    // Gradient Fade
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )
                    
                    ChatInput(
                        text = uiState.inputText,
                        onTextChanged = { viewModel.handleIntent(ChatIntent.UpdateInput(it)) },
                        onSendClick = { viewModel.handleIntent(ChatIntent.SendMessage) },
                        isLoading = uiState.isLoading
                    )
                }
            }
        }
    }
}
