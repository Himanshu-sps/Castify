package com.castify.presentation.screens.videoPlayer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.castify.presentation.common.handleDPadBackKeys

/**
 * Handles the visibility and animation of the controls.
 */
@Composable
fun VideoPlayerOverlay(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    state: VideoPlayerState = rememberVideoPlayerState(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    centerButton: @Composable () -> Unit = {},
    subtitles: @Composable () -> Unit = {},
    controls: @Composable () -> Unit = {}
) {
    LaunchedEffect(state.controlsVisible) {
        if (state.controlsVisible) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            state.showControls(seconds = Int.MAX_VALUE)
        } else {
            state.showControls()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
            .handleDPadBackKeys {
                onBackPressed.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(state.controlsVisible, Modifier, fadeIn(), fadeOut()) {
            CinematicBackground(Modifier.fillMaxSize())
        }

        Column {
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                subtitles()
            }

            AnimatedVisibility(
                state.controlsVisible,
                Modifier,
                slideInVertically { it },
                slideOutVertically { it }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 56.dp)
                        .padding(bottom = 32.dp, top = 8.dp)
                ) {
                    controls()
                }
            }
        }
        centerButton()
    }
}

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
