package com.gurpreetsk.tvdb.model

import com.gurpreetsk.tvdb.LifecycleEvent
import com.gurpreetsk.tvdb.data.FetchAction
import com.gurpreetsk.tvdb.data.FetchEvent
import com.gurpreetsk.tvdb.data.repo.MoviesRepo
import com.gurpreetsk.tvdb.data.model.MovieOverview
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertSame

class PopularMoviesModelTests {
    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun `emit initial state on lifecycle created`() {
        val lifecycle = Channel<LifecycleEvent>()
        val states = Channel<PopularMoviesState>()
        val refresh = Channel<Unit>()
        val retry = Channel<Unit>()
        val intentions = PopularMoviesIntentions(refresh.consumeAsFlow(), retry.consumeAsFlow())
        val repository = mockk<MoviesRepo>()

        val emittedStates = mutableListOf<PopularMoviesState>()

        every { repository.fetchMostPopularMovies() } returns flow<FetchEvent<List<MovieOverview>>> {
            emit(FetchEvent(FetchAction.IN_FLIGHT, null))
            emit(FetchEvent(FetchAction.FETCH_SUCCESS, emptyList()))
        }

        runBlocking {
            PopularMoviesModel
                .bind(
                    lifecycle.consumeAsFlow(),
                    states.consumeAsFlow(),
                    intentions,
                    repository
                )
                .toList(emittedStates)

            lifecycle.send(LifecycleEvent.CREATED)
        }

        assertSame(
            listOf(
                PopularMoviesState(FetchAction.IN_FLIGHT, emptyList()),
                PopularMoviesState(FetchAction.FETCH_SUCCESS, emptyList())
            ),
            emittedStates
        )
    }
}
