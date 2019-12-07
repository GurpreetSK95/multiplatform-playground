package com.gurpreetsk.tvdb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesResponse(
    val results: List<MovieOverview>
)
