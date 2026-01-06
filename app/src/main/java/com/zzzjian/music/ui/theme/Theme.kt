package com.zzzjian.music.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !dark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !dark
        }
    }

    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
