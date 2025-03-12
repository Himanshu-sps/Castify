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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.data.dto.MovieDetailsDTO
import com.castify.data.entities.toMovie
import com.castify.presentation.common.GradientBackground
import com.castify.presentation.common.MovieRow
import com.castify.presentation.common.PosterImage
import com.castify.presentation.screens.home.ItemDirection
import kotlinx.coroutines.delay

// Create a FocusRequester for the first item
//val firstItemFocusRequester = FocusRequester()

/**
 * A composable that displays a horizontally scrollable list of upcoming movies with immersive UI effects.
 * Features include:
 * - Dynamic focus management for TV navigation
 * - Animated background that changes based on the selected movie
 * - Persistent selection state across recompositions
 * - Automatic focus restoration when returning from other screens
 *
 * @param sectionTitle The title to display above the movie list
 * @param upcomingMovies List of movies to display in the horizontal list
 * @param onMovieClick Callback invoked when a movie is selected, provides the selected [MovieDetailsDTO]
 */
@Composable
fun UpcomingMoviesList(
    sectionTitle: String,
    upcomingMovies: List<MovieDetailsDTO>,
    onMovieClick: (MovieDetailsDTO) -> Unit,
    firstItemFocusRequester: FocusRequester
) {
    var isListFocused by remember { mutableStateOf(false) }
    var selectedMovie by remember(upcomingMovies) { mutableStateOf(upcomingMovies.first()) }

    // Request focus for the first item when the screen is launched
    LaunchedEffect(Unit) {
        try {
            delay(100) // Small delay to ensure composition is complete
            firstItemFocusRequester.requestFocus()
        } catch (e: Exception) {
            // Handle potential focus request failures silently
        }
    }

    ImmersiveList(
        modifier = Modifier,
        selectedMovie = selectedMovie,
        isListFocused = isListFocused,
        movieList = upcomingMovies,
        sectionTitle = sectionTitle,
        focusRequester = firstItemFocusRequester,
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

/**
 * Displays an animated background image for the currently selected movie.
 * The background includes entrance and exit animations for smooth transitions.
 *
 * @param movie The movie whose poster should be displayed as background
 * @param visible Whether the background should be visible
 * @param modifier Optional modifier for customizing the layout
 */
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
            PosterImage(movie = it.toMovie(), modifier = Modifier.fillMaxSize())
        }
    }
}

/**
 * Displays the description of the currently selected movie.
 * Shows the movie title and overview with appropriate styling.
 *
 * @param movie The movie whose details should be displayed
 * @param modifier Optional modifier for customizing the layout
 */
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

/**
 * The main container composable that combines all elements of the immersive movie list UI.
 * This includes:
 * - Background image
 * - Movie description
 * - Horizontal scrollable list of movies
 * 
 * The composable handles focus states and provides visual feedback for TV navigation.
 *
 * @param modifier Optional modifier for customizing the layout
 * @param selectedMovie The currently selected movie to display in detail
 * @param isListFocused Whether the movie list currently has focus
 * @param movieList List of movies to display in the horizontal list
 * @param sectionTitle Optional title to display above the movie list
 * @param focusRequester Focus requester for managing TV focus
 * @param onFocusChanged Callback invoked when the focus state of the list changes
 * @param onMovieFocused Callback invoked when a movie receives focus
 * @param onMovieClick Callback invoked when a movie is clicked/selected
 */
@Composable
private fun ImmersiveList(
    modifier: Modifier = Modifier,
    selectedMovie: MovieDetailsDTO,
    isListFocused: Boolean,
    movieList: List<MovieDetailsDTO>,
    sectionTitle: String?,
    focusRequester: FocusRequester,
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