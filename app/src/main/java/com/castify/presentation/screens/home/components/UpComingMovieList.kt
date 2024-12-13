package com.castify.presentation.screens.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.domain.TMDBMovie
import com.castify.presentation.common.MovieRow
import com.castify.presentation.common.PosterImage
import com.castify.presentation.common.gradientOverlay
import com.castify.presentation.screens.home.ItemDirection

@Composable
fun UpcomingMoviesList(
    modifier: Modifier,
    gradientColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
) {

    // Create a FocusRequester for the first item
    val firstItemFocusRequester = remember { FocusRequester() }

    // Request focus for the first item when the screen is launched
    LaunchedEffect(Unit) {
        firstItemFocusRequester.requestFocus()
    }

    val movieList = remember {
        listOf(
            TMDBMovie(
                id = "1",
                name = "On the bridge",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/2_3-300/on-the-bridge.jpg"
            ),
            TMDBMovie(
                id = "2",
                name = "Inventor",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/2_3-300/inventor.jpg"
            ),
            TMDBMovie(
                id = "3",
                name = "On the bridge",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/2_3-300/on-the-bridge.jpg"
            ),
            TMDBMovie(
                id = "4",
                name = "Inventor",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/2_3-300/inventor.jpg"
            ),
            TMDBMovie(
                id = "5",
                name = "Cyber net",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/16_9-400/cyber-net.jpg"
            )
        )
    }

    var isListFocused by remember { mutableStateOf(false) }
    var selectedMovie by remember(movieList) { mutableStateOf(movieList.first()) }

    val sectionTitle = "Upcoming Movies"

    ImmersiveList(
        modifier = modifier,
        selectedMovie = selectedMovie,
        isListFocused = isListFocused,
        gradientColor = gradientColor,
        movieList = movieList,
        sectionTitle = sectionTitle,
        focusRequester = firstItemFocusRequester,
        onMovieClick = {},
        onMovieFocused = {
            selectedMovie = it
        },
        onFocusChanged = {
            isListFocused = it.hasFocus
        }
    )
}

@Composable
private fun ImageBackground(
    movie: TMDBMovie,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Crossfade(
            targetState = movie,
            label = "posterUriCrossfade"
        ) {
            PosterImage(movie = it, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun MovieDescription(
    movie: TMDBMovie,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = movie.name, style = MaterialTheme.typography.headlineMedium)
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = "desc",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun ImmersiveList(
    modifier: Modifier = Modifier,
    selectedMovie: TMDBMovie,
    isListFocused: Boolean,
    gradientColor: Color,
    movieList: List<TMDBMovie>,
    sectionTitle: String?,
    focusRequester: FocusRequester ?= null,
    onFocusChanged: (FocusState) -> Unit,
    onMovieFocused: (TMDBMovie) -> Unit,
    onMovieClick: (TMDBMovie) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        ImageBackground(
            movie = selectedMovie,
            visible = isListFocused,
            modifier = modifier
                .height(432.dp)
                .gradientOverlay(gradientColor)
                .align(Alignment.TopStart)
        )
        if (isListFocused) {
            MovieDescription(
                movie = selectedMovie,
                modifier = modifier
                    .padding(
                        start = 16.dp,
                        top = 60.dp
                    )
                    .align(Alignment.TopStart)
            )
        }

        MovieRow(
            movieList = movieList,
            itemDirection = ItemDirection.Vertical,
            title = sectionTitle,
            showItemTitle = !isListFocused,
            onMovieSelected = onMovieClick,
            onMovieFocused = onMovieFocused,
            focusRequester = focusRequester,
            modifier = Modifier.onFocusChanged(onFocusChanged)
        )
    }
}