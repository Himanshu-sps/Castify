package com.castify.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun CinematicBackground(modifier: Modifier = Modifier) {
    Spacer(
        modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.8f)
                )
            )
        )
    )
}