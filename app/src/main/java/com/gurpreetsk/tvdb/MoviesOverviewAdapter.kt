package com.gurpreetsk.tvdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gurpreetsk.tvdb.data.UrlMap
import com.gurpreetsk.tvdb.data.model.MovieOverview
import kotlinx.android.synthetic.main.layout_list_item_movie_overview.view.*

class MoviesOverviewAdapter(
    private val imageLoader: ImageLoader
) : ListAdapter<MovieOverview, MovieOverviewViewHolder>(MoviesOverviewItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieOverviewViewHolder {
        return MovieOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_list_item_movie_overview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieOverviewViewHolder, position: Int) {
        holder.bind(imageLoader, getItem(position))
    }
}

class MovieOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        imageLoader: ImageLoader,
        overview: MovieOverview
    ) {
        with(itemView) {
            movieOverviewTitle.text = overview.title

            imageLoader.loadImage(
                this.context,
                UrlMap.getImageUrl(overview.posterPath),
                posterImageView,
                R.drawable.movie_overview_placeholder,
                R.drawable.movie_overview_error
            )
        }
    }
}

private class MoviesOverviewItemCallback : DiffUtil.ItemCallback<MovieOverview>() {
    override fun areItemsTheSame(oldItem: MovieOverview, newItem: MovieOverview): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieOverview, newItem: MovieOverview): Boolean =
        oldItem == newItem
}
