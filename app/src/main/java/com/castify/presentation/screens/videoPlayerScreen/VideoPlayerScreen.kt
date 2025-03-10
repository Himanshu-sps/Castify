package com.castify.presentation.screens.videoPlayerScreen

import android.util.Log
import androidx.compose.runtime.Composable
import com.castify.data.entities.Movie

@Composable
fun VideoPlayerScreen(
    movie: Movie?
) {
    Log.e("TAGE", "VideoPlayerScreen: movie id ${movie?.title}", )
}