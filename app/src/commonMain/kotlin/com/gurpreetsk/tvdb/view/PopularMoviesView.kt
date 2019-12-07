package com.gurpreetsk.tvdb.view

import com.gurpreetsk.tvdb.base.BaseView
import com.gurpreetsk.tvdb.data.ApplicationError
import com.gurpreetsk.tvdb.data.FetchAction
import com.gurpreetsk.tvdb.data.model.MovieOverview
import com.gurpreetsk.tvdb.model.PopularMoviesState

interface PopularMoviesView : BaseView<PopularMoviesState> {
    fun showLoading()
    fun hideLoading()
    fun showPopularMovies(movies: List<MovieOverview>)
    fun hideMoviesList()
    fun showError(error: ApplicationError)
    fun hideError()

    override fun render(state: PopularMoviesState) {
        when(state.action) {
            FetchAction.IN_FLIGHT -> {
                showLoading()
                hideMoviesList()
                hideError()
            }

            FetchAction.FETCH_SUCCESS -> {
                hideLoading()
                showPopularMovies(state.movies)
                hideError()
            }

            FetchAction.FETCH_FAILED -> {
                hideLoading()
                hideMoviesList()
                showError(state.error!!)
            }
        }
    }
}
