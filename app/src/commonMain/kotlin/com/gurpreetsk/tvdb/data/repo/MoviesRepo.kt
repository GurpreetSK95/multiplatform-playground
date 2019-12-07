package com.gurpreetsk.tvdb.data.repo

import com.gurpreetsk.tvdb.data.FetchAction
import com.gurpreetsk.tvdb.data.FetchEvent
import com.gurpreetsk.tvdb.data.UrlMap
import com.gurpreetsk.tvdb.data.model.MovieOverview
import com.gurpreetsk.tvdb.data.model.PopularMoviesResponse
import com.gurpreetsk.tvdb.data.toApplicationError
import com.gurpreetsk.tvdb.shared.Schedulers
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

interface MoviesRepo {
    fun fetchMostPopularMovies(): Flow<FetchEvent<List<MovieOverview>>>
}

internal class MoviesRepoImpl(
    private val httpClient: HttpClient,
    private val schedulers: Schedulers
) : MoviesRepo {
    @UnstableDefault
    @ExperimentalCoroutinesApi
    override fun fetchMostPopularMovies(): Flow<FetchEvent<List<MovieOverview>>> {
        val inProgressEvents = listOf(
            FetchEvent(
                FetchAction.IN_FLIGHT,
                null
            )
        )

        val dataEvents = flow { emit(httpClient.get<String>(UrlMap.PopularMovies.mostPopularMovies)) }
            .take(1)
            .map { Json.nonstrict.parse(PopularMoviesResponse.serializer(), it).results }
            .map {
                FetchEvent(
                    FetchAction.FETCH_SUCCESS,
                    it
                )
            }
            .catch { emit(
                FetchEvent(
                    FetchAction.FETCH_FAILED,
                    null,
                    listOf(it.toApplicationError())
                )
            ) }
            .flowOn(schedulers.io())

        return inProgressEvents.asFlow()
            .onCompletion<FetchEvent<List<MovieOverview>>> { emitAll(dataEvents) }
            .flowOn(schedulers.io())
    }
}
