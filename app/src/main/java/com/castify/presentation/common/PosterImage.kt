package com.castify.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.castify.domain.TMDBMovie

@Composable
fun PosterImage(
    movie: TMDBMovie,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .crossfade(true)
            .data(movie.imageUri)
            .build(),
        contentDescription = movie.name,
        contentScale = ContentScale.Crop
    )
}