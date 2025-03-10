package com.castify.data.dto


import kotlinx.serialization.Serializable

/*data class HomePageResponse(
    @SerializedName("result")
    val result: List<Result?>? = listOf()
) {
    data class Result(
        @SerializedName("details")
        val details: List<Detail?>? = listOf(),
        @SerializedName("id")
        val id: Int? = 0,
        @SerializedName("title")
        val title: String? = ""
    )
}*/

@Serializable
data class HomePageResponse(
    val details: List<MovieDetailsDTO> = listOf(),
    val id: Int? = 0,
    val title: String? = ""
)

@Serializable
data class MovieDetailsDTO(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val first_air_date: String? = "",
    val genre_ids: List<Int?>? = listOf(),
    val id: Int? = 0,
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
    val video: Boolean? = false,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0
)