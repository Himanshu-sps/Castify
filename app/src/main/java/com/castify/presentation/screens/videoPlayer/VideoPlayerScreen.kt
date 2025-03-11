package com.castify.presentation.screens.videoPlayer

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.castify.data.entities.Movie
import com.castify.data.utils.StringConstants
import com.castify.presentation.common.handleDPadKeyEvents
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerControlsIcon
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerMainFrame
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerMediaTitle
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerMediaTitleType
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerOverlay
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerPulse
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerPulseState
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerSeeker
import com.castify.presentation.screens.videoPlayer.components.VideoPlayerState
import com.castify.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
import com.castify.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * A composable screen for playing a video.
 *
 * @param onBackPressed The callback to invoke when the user presses the back button.
 */
@Composable
fun VideoPlayerScreen(
    movie: Movie,
    onBackPressed: () -> Unit
) {

    VideoPlayerScreenContent(
        movieDetails = movie,
        onBackPressed = onBackPressed
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreenContent(movieDetails: Movie, onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)

    val exoPlayer = rememberExoPlayer(context)
    LaunchedEffect(exoPlayer, movieDetails) {
        exoPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri(movieDetails.videoUri)
                .setSubtitleConfigurations(
                    if (movieDetails.subtitleUri == null) {
                        emptyList()
                    } else {
                        listOf(
                            MediaItem.SubtitleConfiguration
                                .Builder(Uri.parse(movieDetails.subtitleUri))
                                .setMimeType("application/vtt")
                                .setLanguage("en")
                                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                                .build()
                        )
                    }
                ).build()
        )
        exoPlayer.prepare()
    }

    var contentCurrentPosition by remember { mutableLongStateOf(0L) }
    var isPlaying: Boolean by remember { mutableStateOf(exoPlayer.isPlaying) }
    // This is to update current progress on seek bar
    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = exoPlayer.currentPosition
            isPlaying = exoPlayer.isPlaying
        }
    }

    BackHandler(onBack = onBackPressed)

    val pulseState = rememberVideoPlayerPulseState()

    Box(
        Modifier
            .dPadEvents(
                exoPlayer = exoPlayer,
                videoPlayerState = videoPlayerState,
                pulseState = pulseState
            )
            .focusable()
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply { useController = false }
            },
            update = { it.player = exoPlayer },
            onRelease = { exoPlayer.release() }
        )

        val focusRequester = remember { FocusRequester() }
        VideoPlayerOverlay(
            modifier = Modifier.align(Alignment.BottomCenter),
            focusRequester = focusRequester,
            state = videoPlayerState,
            isPlaying = isPlaying,
            onBackPressed = {
                onBackPressed.invoke()
            },
            centerButton = { VideoPlayerPulse(pulseState) },
            subtitles = { /* TODO Implement subtitles */ },
            controls = {
                VideoPlayerControls(
                    movieDetails,
                    isPlaying,
                    contentCurrentPosition,
                    exoPlayer,
                    videoPlayerState,
                    focusRequester
                )
            }
        )
    }
}

@Composable
fun VideoPlayerControls(
    movieDetails: Movie,
    isPlaying: Boolean,
    contentCurrentPosition: Long,
    exoPlayer: ExoPlayer,
    state: VideoPlayerState,
    focusRequester: FocusRequester
) {
    val onPlayPauseToggle = { shouldPlay: Boolean ->
        if (shouldPlay) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    VideoPlayerMainFrame(
        mediaTitle = {
            VideoPlayerMediaTitle(
                title = movieDetails.title ?: "",
                secondaryText = movieDetails.releaseDate ?: "",
                tertiaryText = "larry",
                type = VideoPlayerMediaTitleType.DEFAULT
            )
        },
        mediaActions = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoPlayerControlsIcon(
                    icon = Icons.Default.AutoAwesomeMotion,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlPlaylistButton
                )
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = Icons.Default.ClosedCaption,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlClosedCaptionsButton
                )
                VideoPlayerControlsIcon(
                    modifier = Modifier.padding(start = 12.dp),
                    icon = Icons.Default.Settings,
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlSettingsButton
                )
            }
        },
        seeker = {
            VideoPlayerSeeker(
                focusRequester,
                state,
                isPlaying,
                onPlayPauseToggle,
                onSeek = { exoPlayer.seekTo(exoPlayer.duration.times(it).toLong()) },
                contentProgress = contentCurrentPosition.milliseconds,
                contentDuration = exoPlayer.duration.milliseconds
            )
        },
        more = null
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun rememberExoPlayer(context: Context) = remember {
    ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(30000)
        .setSeekBackIncrementMs(30000)
        .setMediaSourceFactory(
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
        )
        .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        .build()
        .apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
}

private fun Modifier.dPadEvents(
    exoPlayer: ExoPlayer,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState
): Modifier = this.handleDPadKeyEvents(
    onLeft = {
        if (!videoPlayerState.controlsVisible) {
            exoPlayer.seekBack()
            pulseState.setType(BACK)
        }
    },
    onRight = {
        if (!videoPlayerState.controlsVisible) {
            exoPlayer.seekForward()
            pulseState.setType(FORWARD)
        }
    },
    onUp = { videoPlayerState.showControls() },
    onDown = { videoPlayerState.showControls() },
    onEnter = {
        exoPlayer.pause()
        videoPlayerState.showControls()
    }
)
