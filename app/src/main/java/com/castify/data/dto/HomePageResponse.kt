package com.castify.data.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class HomePageResponse(
    @SerializedName("sectionId")
    val sectionId: String? = "0",
    @SerializedName("details")
    val details: List<MovieDetailsDTO> = listOf(),
    @SerializedName("title")
    val title: String? = ""
)

@Serializable
data class MovieDetailsDTO(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val first_air_date: String? = "",
    val genre_ids: List<Int?>? = listOf(),
    val id: String = "",
    val name: String? = "",
    val origin_country: List<String?>? = listOf(),
    val original_language: String? = "",
    val original_name: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val release_date: String? = "",
    val title: String? = "",
    val videoUri: String? = "",
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0
)