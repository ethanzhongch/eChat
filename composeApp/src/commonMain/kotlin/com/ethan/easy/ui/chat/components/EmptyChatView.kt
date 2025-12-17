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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmptyChatView(
    selectedModel: ChatModel,
    onModelSelected: (ChatModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Model Selector Logic
        // We use a VerticalPager to create a snapping wheel effect.
        // The page size is small to show adjacent items.
        val models = ChatModel.entries
        val pagerState = rememberPagerState(
            initialPage = models.indexOf(selectedModel),
            pageCount = { models.size }
        )

        // Sync pager state with selected model
        LaunchedEffect(pagerState.currentPage) {
            onModelSelected(models[pagerState.currentPage])
        }
        
        // OR: Sync external selection with pager (if needed, bi-directional)
        LaunchedEffect(selectedModel) {
            pagerState.animateScrollToPage(models.indexOf(selectedModel))
        }

        Box(
            modifier = Modifier
                .height(150.dp) // Height of the visible picker area
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            VerticalPager(
                state = pagerState,
                contentPadding = PaddingValues(vertical = 50.dp), // Padding to center the item
                pageSize = PageSize.Fixed(50.dp), // Fixed height for each item
                beyondViewportPageCount = 2
            ) { page ->
                val model = models[page]
                val isSelected = pagerState.currentPage == page
                
                // Animation for scale and alpha based on distance from center
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Logo would go here if we had resources
                        // Icon(painter = painterResource(...), ...) 
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
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "How can I help you today?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
