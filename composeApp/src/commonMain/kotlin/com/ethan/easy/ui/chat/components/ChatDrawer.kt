package com.ethan.easy.ui.chat.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ethan.easy.data.database.SessionEntity
import com.ethan.easy.util.PlatformTime

@Composable
fun ChatDrawer(
    sessions: List<SessionEntity>,
    selectedSessionId: String?,
    onNewChat: () -> Unit,
    onSessionSelected: (String) -> Unit
) {
    val groupedSessions = remember(sessions) {
        val now = PlatformTime.getCurrentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L
        sessions.groupBy { session ->
            val diff = now - session.createdAt
            when {
                diff < dayMillis -> "Today"
                diff < 2 * dayMillis -> "Yesterday"
                else -> "Previous 7 Days"
            }
        }
    }

    ModalDrawerSheet(
        modifier = Modifier.width(320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        drawerTonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: New Chat Button
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 56.dp, bottom = 16.dp)
            ) {
                Surface(
                    onClick = onNewChat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp, 
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color(0xFFECFDF5) // Green-50
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.padding(6.dp),
                                tint = Color(0xFF059669) // Emerald-600
                            )
                        }
                        Text(
                            text = "New Chat",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }

            // Body: Grouped Sessions
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                val categories = listOf("Today", "Yesterday", "Previous 7 Days")
                categories.forEach { category ->
                    val sessionList = groupedSessions[category]
                    if (!sessionList.isNullOrEmpty()) {
                        item {
                            Text(
                                text = category.uppercase(),
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            )
                        }
                        items(sessionList) { session ->
                            DrawerSessionItem(
                                title = session.title,
                                isSelected = session.id == selectedSessionId,
                                onClick = { onSessionSelected(session.id) }
                            )
                        }
                    }
                }
            }

            // Footer: Profile Card
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .padding(bottom = 12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(20.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.1f)),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFF10B981), Color(0xFF14B8A6))
                                    ),
                                    shape = CircleShape
                                )
                                .padding(2.dp)
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                // Default profile icon or image
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.padding(4.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ethan Designer",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "example@email.com",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }

                        IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerSessionItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                else Color.Transparent
            )
            .clickable(onClick = onClick)
    ) {
        if (isSelected) {
            // Emerald Indicator
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(3.dp)
                    .height(20.dp)
                    .background(Color(0xFF10B981), RoundedCornerShape(4.dp))
                    .shadow(elevation = 8.dp, spotColor = Color(0xFF10B981))
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 24.dp)
                .padding(start = if (isSelected) 8.dp else 0.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface 
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            maxLines = 1
        )
    }
}
