package com.gurpreetsk.tvdb

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gurpreetsk.tvdb.data.ApplicationError
import com.gurpreetsk.tvdb.data.model.MovieOverview
import com.gurpreetsk.tvdb.data.repo.MoviesRepo
import com.gurpreetsk.tvdb.data.repo.MoviesRepoImpl
import com.gurpreetsk.tvdb.databinding.ActivityMainBinding
import com.gurpreetsk.tvdb.model.PopularMoviesIntentions
import com.gurpreetsk.tvdb.model.PopularMoviesModel
import com.gurpreetsk.tvdb.model.PopularMoviesState
import com.gurpreetsk.tvdb.base.BaseActivity
import com.gurpreetsk.tvdb.shared.HttpClient
import com.gurpreetsk.tvdb.view.PopularMoviesView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseActivity<PopularMoviesState>(), PopularMoviesView {
    private val refreshClicks = BroadcastChannel<Unit>(1)
    private val retryClicks = BroadcastChannel<Unit>(1)
    private val intentions by lazy {
        PopularMoviesIntentions(
            refreshClicks.asFlow(),
            retryClicks.asFlow()
        )
    }
    private val repository: MoviesRepo by lazy {
        MoviesRepoImpl(
            HttpClient.get(),
            schedulers
        )
    }
    private val imageLoader: ImageLoader = ImageLoaderImpl()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val moviesAdapter by lazy { MoviesOverviewAdapter(imageLoader) }

    override fun setupUi() {
        with(binding) {
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
            recyclerView.adapter = moviesAdapter
        }
    }

    override fun getLayoutRes(): ViewGroup = binding.root

    override fun bind(states: Flow<PopularMoviesState>): Flow<PopularMoviesState> =
        PopularMoviesModel.bind(flowLifecycle(), states, intentions, repository)

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showPopularMovies(movies: List<MovieOverview>) {
        binding.recyclerView.visibility = View.VISIBLE
        moviesAdapter.submitList(movies)
    }

    override fun hideMoviesList() {
        binding.recyclerView.visibility = View.GONE
    }

    override fun showError(error: ApplicationError) {
        binding.errorTextView.visibility = View.VISIBLE
    }

    override fun hideError() {
        binding.errorTextView.visibility = View.GONE
    }
}
