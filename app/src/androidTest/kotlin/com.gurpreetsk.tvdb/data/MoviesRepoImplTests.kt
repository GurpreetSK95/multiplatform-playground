package com.gurpreetsk.tvdb.data

import com.gurpreetsk.tvdb.data.model.MovieOverview
import com.gurpreetsk.tvdb.data.repo.MoviesRepo
import com.gurpreetsk.tvdb.data.repo.MoviesRepoImpl
import com.gurpreetsk.tvdb.shared.Schedulers
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class MoviesRepoImplTests {
    @Test
    fun `fetching data from remote fails due to network failure`() {
        val schedulers = mockk<Schedulers>()
        val mockHttpClient = mockk<HttpClient>()
        val repo: MoviesRepo =
            MoviesRepoImpl(
                mockHttpClient,
                schedulers
            )
        val events = mutableListOf<FetchEvent<List<MovieOverview>>>()

        every { runBlocking { mockHttpClient.get<String>(UrlMap.PopularMovies.mostPopularMovies) } } returns ""
        every { schedulers.io() } returns Dispatchers.Unconfined

        runBlocking {
            repo.fetchMostPopularMovies().toList(events)
        }

        assertEquals(
            listOf<FetchEvent<List<MovieOverview>>>(
                FetchEvent(FetchAction.IN_FLIGHT, null),
                FetchEvent(
                    FetchAction.FETCH_FAILED,
                    null,
                    listOf(ApplicationError(ErrorType.UNKNOWN))
                )
            ),
            events
        )
    }

    @Test
    fun `data deserialization fails`() {
        val schedulers = mockk<Schedulers>()
        val mockHttpClient = mockk<HttpClient>()
        val repo: MoviesRepo =
            MoviesRepoImpl(
                mockHttpClient,
                schedulers
            )
        val events = mutableListOf<FetchEvent<List<MovieOverview>>>()

        every { runBlocking { mockHttpClient.get<String>(UrlMap.PopularMovies.mostPopularMovies) } } returns """
            "results": {}
        """.trimIndent()
        every { schedulers.io() } returns Dispatchers.Unconfined

        runBlocking {
            events.addAll(repo.fetchMostPopularMovies().toList())
        }

        assertEquals(
            listOf<FetchEvent<List<MovieOverview>>>(
                FetchEvent(FetchAction.IN_FLIGHT, null),
                FetchEvent(
                    FetchAction.FETCH_FAILED,
                    null,
                    listOf(ApplicationError(ErrorType.UNKNOWN))
                )
            ),
            events
        )
    }

    @Test // No clue why is this failing :(
    fun `fetching data from remote succeeds`() {
        val schedulers = mockk<Schedulers>()
        val mockHttpClient = mockk<HttpClient>()
        val repo: MoviesRepo =
            MoviesRepoImpl(
                mockHttpClient,
                schedulers
            )
        val events = mutableListOf<FetchEvent<List<MovieOverview>>>()

        every { runBlocking { mockHttpClient.get<String>(UrlMap.PopularMovies.mostPopularMovies) } } returns "{ \"results\": [] }"
        every { schedulers.io() } returns Dispatchers.Unconfined

        runBlocking {
            repo.fetchMostPopularMovies().toList(events)
        }

        assertEquals(
            listOf<FetchEvent<List<MovieOverview>>>(
                FetchEvent(FetchAction.IN_FLIGHT, null),
                FetchEvent(
                    FetchAction.FETCH_SUCCESS,
                    emptyList()
                )
            ),
            events
        )
    }
}
