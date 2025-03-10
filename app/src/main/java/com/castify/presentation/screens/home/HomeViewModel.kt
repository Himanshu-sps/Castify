package com.castify.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castify.data.entities.toMovie
import com.castify.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeListState())
    val state = _state
        .onStart { onEvent(HomeEvent.LoadMovies) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeListState()
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadMovies -> getAllMovies()
            is HomeEvent.GetMovieDetails -> getMovieDetails(event.movieId)
            is HomeEvent.ClearSelectedMovie -> clearSelectedMovie()
        }
    }

    private fun clearSelectedMovie() {
        _state.update { currentState ->
            currentState.copy(selectedMovie = null)
        }
    }

    /**
     * Get all movies
     *
     */
    private fun getAllMovies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val homePageResponse = movieRepository.getHomePageDataFromAssets()
                val upcomingMovies = homePageResponse.first { it.title == "Upcoming Movies" }.details

                _state.update { homeListState ->
                    homeListState.copy(
                        isLoading = false,
                        allMovies = homePageResponse,
                        upcomingMovies = upcomingMovies
                    )
                }

                // Set initial selected movie
                /*upcomingMovies.firstOrNull()?.id?.let { movieId ->
                    onEvent(HomeEvent.GetMovieDetails(movieId))
                }*/

            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun getMovieDetails(movieId: String?) {
        viewModelScope.launch {
            try {
                val selectedMovie = movieId?.let { id ->
                    _state.value.upcomingMovies
                        .find { it.id == id }
                        ?.toMovie()
                }
                _state.update { currentState ->
                    currentState.copy(selectedMovie = selectedMovie)
                }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

sealed class HomeEvent {
    data object LoadMovies : HomeEvent()
    data class GetMovieDetails(val movieId: String) : HomeEvent()
    data object ClearSelectedMovie : HomeEvent()
}