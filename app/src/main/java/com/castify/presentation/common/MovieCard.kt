package com.castify.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import com.castify.ui.CastifyBorderWidth
import com.castify.ui.CastifyCardShape

@Composable
fun MovieCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    image: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = ClickableSurfaceDefaults.shape(CastifyCardShape),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = CastifyBorderWidth,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                shape = CastifyCardShape
            )
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
        content = image
    )
}