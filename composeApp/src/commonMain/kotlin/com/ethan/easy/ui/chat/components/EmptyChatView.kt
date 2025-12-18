package com.ethan.easy.ui.chat.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ethan.easy.ui.chat.ChatModel
import kotlin.math.absoluteValue

/**
 * State A: Empty / New Chat View
 * Contains the crucial Model Selector (Wheel Picker).
 */
import androidx.compose.foundation.clickable

/**
 * State A: Empty / New Chat View
 * Contains the crucial Model Selector (Wheel Picker).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmptyChatView(
    selectedModel: ChatModel,
    onModelSelected: (ChatModel) -> Unit,
    isKeyMissing: Boolean,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... (Pager logic remains same)
        val models = ChatModel.entries
        val pagerState = rememberPagerState(
            initialPage = models.indexOf(selectedModel),
            pageCount = { models.size }
        )

        LaunchedEffect(pagerState.currentPage) {
            onModelSelected(models[pagerState.currentPage])
        }

        LaunchedEffect(selectedModel) {
            pagerState.animateScrollToPage(models.indexOf(selectedModel))
        }

        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            VerticalPager(
                state = pagerState,
                contentPadding = PaddingValues(vertical = 50.dp),
                pageSize = PageSize.Fixed(50.dp),
                beyondViewportPageCount = 2
            ) { page ->
                val model = models[page]
                val isSelected = pagerState.currentPage == page
                
                val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue

                val scale = 1f - (pageOffset * 0.3f)
                val alpha = 1f - (pageOffset * 0.6f)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .graphicsLayer {
                            scaleX = scale.coerceAtLeast(0.5f)
                            scaleY = scale.coerceAtLeast(0.5f)
                            this.alpha = alpha.coerceAtLeast(0.3f)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = model.displayName,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isKeyMissing) {
            Text(
                text = "⚠️ API Key missing. Tap to configure.",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable { onNavigateToSettings() }
            )
        } else {
            Text(
                text = "How can I help you today?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
