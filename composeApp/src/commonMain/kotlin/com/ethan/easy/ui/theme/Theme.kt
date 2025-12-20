package com.ethan.easy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Custom Color Palette from Template
val PrimaryBlue = Color(0xFF3B82F6)
val PrimarySoft = Color(0xFFEFF6FF)
val BackgroundLight = Color(0xFFFCFCFC)
val BackgroundDark = Color(0xFF101922)
val SurfaceDark = Color(0xFF1E293B)

// Message Bubble Colors
val UserBubbleLight = Color(0xFFEFFDF5) // Greenish soft
val UserBubbleDark = Color(0xFF064E3B).copy(alpha = 0.2f)
val UserBubbleBorderLight = Color(0xFFECFDF5)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = UserBubbleLight,
    onPrimaryContainer = Color(0xFF1E293B),
    background = BackgroundLight,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9), // Gray-100/50 mix
    onSurface = Color(0xFF334155), // Slate-700
    outline = Color(0xFFE2E8F0), // Gray-200
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = UserBubbleDark,
    onPrimaryContainer = Color(0xFFECFDF5),
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF334155),
    onSurface = Color(0xFFCBD5E1), // Slate-300
    outline = Color(0xFF334155),
    errorContainer = Color(0xFF7F1D1D).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFFCA5A5)
)

@Composable
fun EChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
