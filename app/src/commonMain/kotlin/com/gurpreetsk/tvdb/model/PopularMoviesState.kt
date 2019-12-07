package com.gurpreetsk.tvdb.model

import com.gurpreetsk.tvdb.data.ApplicationError
import com.gurpreetsk.tvdb.data.FetchAction
import com.gurpreetsk.tvdb.data.model.MovieOverview
import com.gurpreetsk.tvdb.shared.Restorable
import com.gurpreetsk.tvdb.shared.SaveState

@SaveState
data class PopularMoviesState(
    val action: FetchAction,
    val movies: List<MovieOverview>,
    val error: ApplicationError? = null
) : Restorable
