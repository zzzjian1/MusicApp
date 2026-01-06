package com.zzzjian.music.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF3B82F6),
    secondary = androidx.compose.ui.graphics.Color(0xFF1E3A8A)
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1E3A8A),
    secondary = androidx.compose.ui.graphics.Color(0xFF3B82F6)
)

@Composable
fun MusicTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
