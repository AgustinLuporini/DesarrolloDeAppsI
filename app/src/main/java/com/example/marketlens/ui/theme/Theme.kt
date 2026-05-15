package com.example.marketlens.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MarketGreen,
    background = MarketBlack,
    surface = DarkGray,
    onPrimary = MarketBlack,
    onBackground = MarketGreen,
    onSurface = MarketGreen
)

@Composable
fun MarketLensTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}