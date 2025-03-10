package com.castify.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        .onStart { getAllMovies() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeListState()
        )

    private fun getAllMovies() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val homePageResponse = movieRepository.getHomePageDataFromAssets()

            _state.update { homeListState ->
                homeListState.copy(
                    isLoading = false,
                    allMovies = homePageResponse,
                    upcomingMovies = homePageResponse.first { it.title == "Upcoming Movies" }.details
                )
            }

            /*movieRepository
                .getHomePageData()
                .onSuccess { allMovies ->
                    Log.e("TAGX", "etAllMovies: $allMovies")

                    _state.update { homeListState ->
                        homeListState.copy(
                            isLoading = false,
                            allMovies = allMovies,
                            upcomingMovies = allMovies.first { it.title == "Upcoming Movies" }.movieDetailsDTO
                        )
                    }
                }
                .onError { error ->
                    Log.e("TAGX", "on error getAllMovies: $error")
                    _state.update { it.copy(isLoading = false) }
                }*/
        }
    }

}