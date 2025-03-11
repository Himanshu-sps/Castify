package com.castify.data.entities

import com.castify.data.dto.MovieDetailsDTO
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String?,
    val videoUri: String?,
    val title: String?,
    val posterUri: String?,
    val overview: String?,
    val releaseDate: String?,
    val isAdult: Boolean?,
    val subtitleUri: String?,
)

fun MovieDetailsDTO.toMovie(): Movie {
    return Movie(
        id = id,
        videoUri = videoUri,
        title = title,
        posterUri = poster_path,
        overview = overview,
        releaseDate = release_date,
        isAdult = adult ?: false,
        subtitleUri = "hello"
    )
}