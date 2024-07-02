package com.tobs.anotador.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = Color.Black,
    secondary = secondary,
    onSecondary = Color.White,
    background = primary,
    onBackground = Color.Black,
    surface = Color.DarkGray

    /* Other default colors to override
    tertiary = tertiary,
    onTertiary = Color.Black,
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = Color.Black,
    secondary = secondary,
    onSecondary = Color.White,
    background = primary,
    onBackground = Color.Black,
    surface = Color.White

    /* Other default colors to override
    tertiary = tertiary,
    onTertiary = Color.Black,
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AnotadorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}