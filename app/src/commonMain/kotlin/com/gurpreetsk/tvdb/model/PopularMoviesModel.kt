package com.gurpreetsk.tvdb.model

import com.gurpreetsk.tvdb.LifecycleEvent
import com.gurpreetsk.tvdb.data.repo.MoviesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

object PopularMoviesModel {
    @ExperimentalCoroutinesApi
    @FlowPreview
    fun bind(
        lifecycle: Flow<LifecycleEvent>,
        states: Flow<PopularMoviesState>,
        intentions: PopularMoviesIntentions,
        repository: MoviesRepo
    ): Flow<PopularMoviesState> {
        val lifecycleCreatedStates = lifecycle
            .filter { it == LifecycleEvent.CREATED }
            .flatMapLatest {
                repository
                    .fetchMostPopularMovies()
                    .map { PopularMoviesState(it.action, it.result.orEmpty(), it.errors?.get(0)) }
            }

        val refreshStates = intentions.refresh
            .combine(states) { _, state ->
                repository
                    .fetchMostPopularMovies()
                    .map { state.copy(it.action, it.result.orEmpty(), it.errors?.get(0)) }
            }
            .flattenMerge()

        val retryStates = intentions.retry
            .combine(states) { _, state ->
                repository
                    .fetchMostPopularMovies()
                    .map { state.copy(it.action, it.result.orEmpty(), it.errors?.get(0)) }
            }
            .flattenMerge()

        return flowOf(lifecycleCreatedStates, refreshStates, retryStates)
            .flattenMerge()
    }
}
