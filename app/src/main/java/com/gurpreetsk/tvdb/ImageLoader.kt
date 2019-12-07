package com.gurpreetsk.tvdb

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

interface ImageLoader {
    fun loadImage(
        context: Context,
        url: String,
        target: ImageView,
        @DrawableRes placeholder: Int? = null,
        @DrawableRes error: Int? = null
    )
}

class ImageLoaderImpl : ImageLoader {
    override fun loadImage(
        context: Context,
        url: String,
        target: ImageView,
        @DrawableRes placeholder: Int?,
        @DrawableRes error: Int?
    ) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.movie_overview_placeholder)
            .error(R.drawable.movie_overview_error)
            .into(target)
    }
}
