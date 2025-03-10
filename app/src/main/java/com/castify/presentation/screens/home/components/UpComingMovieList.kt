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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.data.dto.MovieDetailsDTO
import com.castify.presentation.common.GradientBackground
import com.castify.presentation.common.MovieRow
import com.castify.presentation.common.PosterImage
import com.castify.presentation.screens.home.ItemDirection

// Create a FocusRequester for the first item
//val firstItemFocusRequester = FocusRequester()

@Composable
fun UpcomingMoviesList(
    sectionTitle: String,
    upcomingMovies: List<MovieDetailsDTO>,
    onMovieClick: (MovieDetailsDTO) -> Unit
) {
    // Request focus for the first item when the screen is launched
    /*LaunchedEffect(Unit) {
        firstItemFocusRequester.requestFocus()
    }*/

    var isListFocused by remember { mutableStateOf(false) }
    var selectedMovie by remember(upcomingMovies) { mutableStateOf(upcomingMovies.first()) }

    ImmersiveList(
        modifier = Modifier,
        selectedMovie = selectedMovie,
        isListFocused = isListFocused,
        movieList = upcomingMovies,
        sectionTitle = sectionTitle,
        focusRequester = null,
        onMovieClick = {
            onMovieClick.invoke(it)
        },
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
    movie: MovieDetailsDTO,
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
    movie: MovieDetailsDTO,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = movie.title ?: "Title", style = MaterialTheme.typography.headlineMedium)
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = movie.overview ?: "Description",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun ImmersiveList(
    modifier: Modifier = Modifier,
    selectedMovie: MovieDetailsDTO,
    isListFocused: Boolean,
    movieList: List<MovieDetailsDTO>,
    sectionTitle: String?,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (FocusState) -> Unit,
    onMovieFocused: (MovieDetailsDTO) -> Unit,
    onMovieClick: (MovieDetailsDTO) -> Unit
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
                .align(Alignment.TopStart)
        )

        GradientBackground(Modifier.fillMaxSize())

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