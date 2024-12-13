package com.castify.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

@Composable
fun CastifyTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isInDarkTheme) {
        darkColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40,
            surface = Black,
            onSurface = White,
            background = Gray,
            onBackground = White
        )
    } else {
        lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40,
            surface = Black,
            onSurface = White,
            background = Gray,
            onBackground = White
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val CastifyCardShape = ShapeDefaults.ExtraSmall
val CastifyButtonShape = ShapeDefaults.ExtraSmall
val IconSize = 20.dp
val CastifyBorderWidth = 3.dp

/**
 * Space to be given below every Lazy (or scrollable) vertical list throughout the app
 */
val CastifyBottomListPadding = 28.dp