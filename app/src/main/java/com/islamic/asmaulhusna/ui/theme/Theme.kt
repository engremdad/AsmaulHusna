package com.islamic.asmaulhusna.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Mushaf is a single, committed dark world — the same illuminated palette in
// every lighting condition. We ignore the system light/dark setting on purpose.
private val MushafColors = darkColorScheme(
    primary            = Gold,
    onPrimary          = GoldInk,
    primaryContainer   = Emerald,
    onPrimaryContainer = Cream,
    secondary          = GoldSoft,
    onSecondary        = GoldInk,
    secondaryContainer = EmeraldHi,
    onSecondaryContainer = Cream,
    background         = Page,
    onBackground       = Cream,
    surface            = EmeraldRow,
    onSurface          = Cream,
    surfaceVariant     = SectGround,
    onSurfaceVariant   = Cream,
    outline            = GoldDim,
    outlineVariant     = EmeraldLine,
    scrim              = Ink
)

@Composable
fun AsmaulHusnaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MushafColors,
        typography = Typography,
        content = content
    )
}
