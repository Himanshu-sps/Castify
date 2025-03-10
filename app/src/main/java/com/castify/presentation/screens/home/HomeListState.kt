package com.castify.presentation.screens.home

import com.castify.data.dto.HomePageResponse
import com.castify.data.dto.MovieDetailsDTO

data class HomeListState(
    val isLoading: Boolean = false,
    val allMovies: List<HomePageResponse> = emptyList(),

    val upcomingMovies: List<MovieDetailsDTO> = emptyList(),
    val popularMovies: List<MovieDetailsDTO> = emptyList()
)