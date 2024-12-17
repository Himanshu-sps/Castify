package com.castify.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    gradientColor: Color = MaterialTheme.colorScheme.background
) {
    Spacer(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        gradientColor,
                        gradientColor,
                        gradientColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    startX = 0f,
                    endX = Float.POSITIVE_INFINITY
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        gradientColor.copy(alpha = 0.3f),
                        gradientColor,
                        gradientColor
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    )
}