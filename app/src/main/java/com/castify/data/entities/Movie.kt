package com.castify.data.entities

import com.castify.data.dto.MovieDetailsDTO
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String?,
    val videoUri: String?,
    val title: String?,
    val posterUri: String?,
    val overview: String?
)

fun MovieDetailsDTO.toMovie(): Movie {
    return Movie(
        id = id,
        videoUri = videoUri,
        title = title,
        posterUri = poster_path,
        overview = overview
    )
}