package com.gurpreetsk.tvdb.data.model

import com.gurpreetsk.tvdb.shared.Restorable
import com.gurpreetsk.tvdb.shared.SaveState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SaveState
@Serializable
data class MovieOverview(
    val id: Int,
    val popularity: Double,
    val title: String,
    val overview: String,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("adult") val isAdult: Boolean,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("poster_path") val posterPath: String
) : Restorable
