package com.islamic.asmaulhusna.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = IslamicGreen,
    onPrimary = OffWhite,
    primaryContainer = IslamicGreenLight,
    onPrimaryContainer = OffWhite,
    secondary = Gold,
    onSecondary = InkBrown,
    secondaryContainer = GoldLight,
    onSecondaryContainer = InkBrown,
    background = OffWhite,
    onBackground = InkBrown,
    surface = OffWhite,
    onSurface = InkBrown,
    surfaceVariant = OffWhiteDim,
    onSurfaceVariant = InkBrown,
    outline = Gold
)

private val DarkColors = darkColorScheme(
    primary = GoldLight,
    onPrimary = IslamicGreenDark,
    primaryContainer = IslamicGreen,
    onPrimaryContainer = OffWhite,
    secondary = Gold,
    onSecondary = IslamicGreenDark,
    background = IslamicGreenDark,
    onBackground = OffWhite,
    surface = IslamicGreen,
    onSurface = OffWhite,
    surfaceVariant = IslamicGreenLight,
    onSurfaceVariant = OffWhite,
    outline = GoldLight
)

@Composable
fun AsmaulHusnaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
