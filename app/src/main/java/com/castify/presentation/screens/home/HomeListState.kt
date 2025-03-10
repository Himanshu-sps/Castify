package com.castify.presentation.screens.home

import com.castify.data.dto.HomePageResponse
import com.castify.data.dto.MovieDetailsDTO
import com.castify.data.entities.Movie

data class HomeListState(
    val isLoading: Boolean = false,
    val allMovies: List<HomePageResponse> = emptyList(),

    val upcomingMovies: List<MovieDetailsDTO> = emptyList(),
    val popularMovies: List<MovieDetailsDTO> = emptyList(),

    val selectedMovie: Movie ?= null
)