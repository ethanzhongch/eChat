package com.ethan.easy.ui.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ethan.easy.ui.chat.ChatMessage
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

/**
 * State B: Active Chat View
 * Displays a list of messages.
 */
@Composable
fun MessageListView(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 140.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            MessageBubble(message)
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.isUser
    val isSystem = message.role == "system"
    val isAI = message.role == "assistant"
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = when {
            isSystem -> Alignment.CenterHorizontally
            isUser -> Alignment.End
            else -> Alignment.Start
        }
    ) {
        if (isSystem) {
            SystemMessage(message.content)
            return@Column
        }

        // Message Content Card
        Surface(
            shape = when {
                isUser -> RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
                else -> RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
            },
            color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = if (isUser) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) 
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            ),
            modifier = Modifier
                .widthIn(max = 320.dp)
                .shadow(
                    elevation = if (isUser) 1.dp else 4.dp,
                    shape = if (isUser) RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
                            else RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
        ) {
            if (isAI) {
                Markdown(
                    content = message.content,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    colors = markdownColor(
                        text = MaterialTheme.colorScheme.onSurface,
                        codeBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    typography = markdownTypography(
                        text = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        code = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                )
            } else {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        if (isAI) {
            AIAssistantActions()
        }
    }
}

@Composable
private fun SystemMessage(content: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun AIAssistantActions() {
    Row(
        modifier = Modifier.padding(top = 8.dp, start = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(Icons.Outlined.ThumbUp)
        ActionButton(Icons.Outlined.ThumbDown)
        VerticalDivider(modifier = Modifier.height(16.dp).padding(horizontal = 4.dp), thickness = 1.dp)
        ActionButton(Icons.Outlined.ContentCopy)
        ActionButton(Icons.Outlined.Autorenew)
    }
}

@Composable
private fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    IconButton(
        onClick = {},
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}
