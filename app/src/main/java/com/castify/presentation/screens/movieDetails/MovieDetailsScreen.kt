package com.castify.presentation.screens.movieDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.data.entities.Movie
import com.castify.presentation.common.CastifyOutlineIconButton
import com.castify.presentation.common.GradientBackground
import com.castify.presentation.common.PosterImage
import com.castify.presentation.screens.home.HomeEvent
import com.castify.presentation.screens.home.HomeViewModel
import com.castify.ui.CastifyStartContainerPadding
import com.castify.ui.CastifyTopContainerPadding
import kotlinx.coroutines.delay

@Composable
fun MovieDetailsScreen(
    movieId: String,
    homeViewModel: HomeViewModel,
    moveToVideoPlayerScreen: (movie: Movie) -> Unit
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val watchNowFocusRequester = remember { FocusRequester() }

    // Effect to handle cleanup when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            homeViewModel.onEvent(HomeEvent.ClearSelectedMovie)
        }
    }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.onEvent(HomeEvent.GetMovieDetails(movieId = movieId))
    }

    // Separate effect for focus management
    LaunchedEffect(key1 = homeState.selectedMovie) {
        if (homeState.selectedMovie != null) {
            try {
                // Add a small delay to ensure the UI is ready
                delay(100)
                watchNowFocusRequester.requestFocus()
            } catch (e: Exception) {
                // Handle potential focus request failures silently
            }
        }
    }

    if (homeState.selectedMovie != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            PosterImage(
                modifier = Modifier
                    .width(650.dp)
                    .height(400.dp)
                    .align(Alignment.TopEnd),
                movie = Movie(
                    id = movieId,
                    title = homeState.selectedMovie?.title,
                    posterUri = homeState.selectedMovie?.posterUri,
                    videoUri = homeState.selectedMovie?.videoUri,
                    overview = homeState.selectedMovie?.overview,
                    releaseDate = homeState.selectedMovie?.releaseDate,
                    isAdult = homeState.selectedMovie?.isAdult,
                    subtitleUri = homeState.selectedMovie?.subtitleUri
                )
            )

            GradientBackground(
                modifier = Modifier.fillMaxSize(),
                gradientColor = MaterialTheme.colorScheme.background
            )

            Column(
                modifier = Modifier
                    .padding(
                        start = CastifyStartContainerPadding,
                        top = CastifyTopContainerPadding
                    )
                    .fillMaxWidth(0.4f)
            ) {
                Text(
                    text = homeState.selectedMovie!!.title ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text = homeState.selectedMovie!!.overview ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(50.dp))

                CastifyOutlineIconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(watchNowFocusRequester),
                    icon = Icons.Outlined.PlayArrow,
                    roundPercentage = 20,
                    isFocusedGlow = true,
                    buttonText = "Watch now",
                    onClick = {
                        homeState.selectedMovie?.let {
                            moveToVideoPlayerScreen.invoke(it)
                        }
                    }
                )

                Spacer(modifier = Modifier.size(20.dp))

                CastifyOutlineIconButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Outlined.FavoriteBorder,
                    roundPercentage = 20,
                    isFocusedGlow = true,
                    buttonText = "Add to Favourites",
                    onClick = {}
                )
            }

        }
    }
}