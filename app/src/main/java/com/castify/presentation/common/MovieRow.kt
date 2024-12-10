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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
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
import com.castify.domain.TMDBMovie
import com.castify.presentation.screens.home.ItemDirection
import com.castify.presentation.screens.home.rememberChildPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MovieRow(
    movieList: List<TMDBMovie>,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Medium
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    onMovieSelected: (TMDBMovie) -> Unit = {},
    onMovieFocused: (TMDBMovie) -> Unit = {}
) {
    val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }
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
        ) { movieState ->
            LazyRow(
                contentPadding = PaddingValues(start = startPadding, end = endPadding),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .focusRequester(lazyRow)
                    .focusRestorer {
                        firstItem
                    }
            ) {
                itemsIndexed(
                    movieState,
                    key = { _, movie ->
                        movie.id
                    }
                ) { index, movie ->
                    val itemModifier = if (index == 0) {
                        Modifier.focusRequester(firstItem)
                    } else {
                        Modifier
                    }
                    MoviesRowItem(
                        modifier = itemModifier.weight(1f),
                        index = index,
                        itemDirection = itemDirection,
                        onMovieSelected = {
                            lazyRow.saveFocusedChild()
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviesRowItem(
    index: Int,
    movie: TMDBMovie,
    onMovieSelected: (TMDBMovie) -> Unit,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    onMovieFocused: (TMDBMovie) -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    val imageWidth = if (itemDirection == ItemDirection.Vertical) 160.dp else 220.dp
    val imageHeight = if (itemDirection == ItemDirection.Vertical) 220.dp else 160.dp

    MovieCard(
        onClick = { onMovieSelected(movie) },
        title = {
            if (showItemTitle) {
                val movieNameAlpha by animateFloatAsState(
                    targetValue = if (isFocused) 1f else 0f,
                    label = "",
                )
                Text(
                    text = movie.name,
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
            .focusProperties {
                left = if (index == 0) {
                    FocusRequester.Cancel
                } else {
                    FocusRequester.Default
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

@Composable
private fun MoviesRowItemImage(
    movie: TMDBMovie,
    showIndexOverImage: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        PosterImage(
            movie = movie,
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