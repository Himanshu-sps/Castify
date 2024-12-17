package com.castify.presentation.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.castify.data.dto.TMDBMovie
import com.castify.presentation.screens.home.components.UpcomingMoviesList

@Composable
fun HomeScreen(
    onMovieClick: (TMDBMovie) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        // Section : 1 Immersive list
        item(contentType = "Upcoming Movies") {
            UpcomingMoviesList(
                onMovieClick = onMovieClick
            )
        }
    }
}

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(10.5f / 16f),
    Horizontal(16f / 9f);
}