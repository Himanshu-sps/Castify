package com.castify.presentation.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.data.dto.MovieDetailsDTO
import com.castify.data.entities.toMovie
import com.castify.presentation.screens.home.ItemDirection

/**
 * A reusable composable that displays a horizontal row of movie items optimized for TV navigation.
 * Supports dynamic focus management, animations, and customizable layout options.
 *
 * Features:
 * - TV-optimized focus management with [FocusRequester]s
 * - Optional section title
 * - Customizable item layout (vertical/horizontal)
 * - Animated content transitions
 * - Focus-based visual feedback
 *
 * @param movieList List of movies to display in the row
 * @param modifier Optional modifier for customizing the layout
 * @param itemDirection Direction of individual movie items (vertical/horizontal orientation)
 * @param startPadding Padding at the start of the row
 * @param endPadding Padding at the end of the row
 * @param title Optional title to display above the row
 * @param titleStyle Style to apply to the title text
 * @param showItemTitle Whether to show titles below movie items
 * @param showIndexOverImage Whether to show index numbers over movie posters
 * @param focusRequesters List of focus requesters for TV navigation, one per movie item
 * @param onMovieSelected Callback invoked when a movie is selected
 * @param onMovieFocused Callback invoked when a movie receives focus
 */
@Composable
fun MovieRow(
    movieList: List<MovieDetailsDTO>,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = 16.dp,
    endPadding: Dp = 16.dp,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Medium
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusRequesters: List<FocusRequester> = emptyList(),
    onMovieSelected: (MovieDetailsDTO) -> Unit = {},
    onMovieFocused: (MovieDetailsDTO) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 20.dp)
            )
        }

        AnimatedContent(
            targetState = movieList,
            label = "",
        ) { tmdbMovieList ->
            LazyRow(
                contentPadding = PaddingValues(start = startPadding, end = endPadding),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                itemsIndexed(
                    items = tmdbMovieList,
                    key = { _, movie -> movie.id!! }
                ) { index, movie ->
                    val itemModifier = if (index < focusRequesters.size) {
                        Modifier.focusRequester(focusRequesters[index])
                    } else Modifier

                    MoviesRowItem(
                        modifier = itemModifier.weight(1f),
                        index = index,
                        itemDirection = itemDirection,
                        onMovieSelected = {
                            onMovieSelected(it)
                        },
                        onMovieFocused = onMovieFocused,
                        movie = movie,
                        showItemTitle = showItemTitle,
                        showIndexOverImage = showIndexOverImage
                    )
                }
            }
        }
    }
}

/**
 * A single item in the movie row that displays a movie poster with optional title and index.
 * Handles focus states and provides visual feedback for TV navigation.
 *
 * Features:
 * - Focus-based animations
 * - Optional title display
 * - Optional index overlay
 * - Customizable dimensions based on orientation
 *
 * @param index Position of the item in the list
 * @param movie Movie data to display
 * @param onMovieSelected Callback invoked when this item is selected
 * @param showItemTitle Whether to show the movie title
 * @param showIndexOverImage Whether to show the index number over the poster
 * @param modifier Optional modifier for customizing the layout
 * @param itemDirection Direction/orientation of the item
 * @param onMovieFocused Callback invoked when this item receives focus
 */
@Composable
fun MoviesRowItem(
    index: Int,
    movie: MovieDetailsDTO,
    onMovieSelected: (MovieDetailsDTO) -> Unit,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    onMovieFocused: (MovieDetailsDTO) -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    val imageWidth = if (itemDirection == ItemDirection.Vertical) 120.dp else 170.dp
    val imageHeight = if (itemDirection == ItemDirection.Vertical) 170.dp else 120.dp

    MovieCard(
        onClick = { onMovieSelected(movie) },
        title = {
            if (showItemTitle) {
                val movieNameAlpha by animateFloatAsState(
                    targetValue = if (isFocused) 1f else 0f,
                    label = "",
                )
                Text(
                    text = movie.name ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .alpha(movieNameAlpha)
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        modifier = Modifier
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) {
                    onMovieFocused(movie)
                }
            }
            .width(imageWidth)
            .height(imageHeight)
            .then(modifier)
    ) {
        MoviesRowItemImage(
            modifier = modifier,
            showIndexOverImage = showIndexOverImage,
            movie = movie,
            index = index
        )
    }
}

/**
 * Displays a movie poster image with an optional index overlay.
 * Handles the layout and styling of the poster image and index number.
 *
 * @param movie Movie data containing the poster image information
 * @param showIndexOverImage Whether to show the index number overlay
 * @param index Position of the movie in the list
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
private fun MoviesRowItemImage(
    movie: MovieDetailsDTO,
    showIndexOverImage: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        PosterImage(
            movie = movie.toMovie(),
            modifier = modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    if (showIndexOverImage) {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0.1f
                            )
                        )
                    }
                },
        )
        if (showIndexOverImage) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "#${index.inc()}",
                style = MaterialTheme.typography.displayLarge
                    .copy(
                        shadow = Shadow(
                            offset = Offset(0.5f, 0.5f),
                            blurRadius = 5f
                        ),
                        color = Color.White
                    ),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}