package com.deeosoft.deonews.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val PastelDarkColorPalette = darkColors(
    primary = TitleBlack,
    primaryVariant = ToolbarBlack,
    secondary = white
)
val PastelLightColorPalette = darkColors(
    primary = TitleWhite,
    primaryVariant = TitleWhite,
    secondary = black
)

private val DarkColorPalette = darkColors(
    primary = ToolbarBlack,
    primaryVariant = Color.Black,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = ToolbarBlack,
    primaryVariant = Color.Black,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PastelTestTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        PastelDarkColorPalette
    } else {
        PastelLightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}