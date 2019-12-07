package com.gurpreetsk.tvdb.model

import kotlinx.coroutines.flow.Flow

data class PopularMoviesIntentions(
    val refresh: Flow<Unit>,
    val retry: Flow<Unit>
)
