package com.gurpreetsk.tvdb.data

import com.gurpreetsk.tvdb.shared.BuildConfig

private const val baseUrl: String = "https://api.themoviedb.org/3/"
private const val imageBaseUrl: String = "https://image.tmdb.org/t/p/original/"

object UrlMap {
    fun getImageUrl(path: String): String {
        return "$imageBaseUrl$path?api_key=${BuildConfig.apiKey}" // TODO(gs) Can API_KEY be sent in header?
    }

    object PopularMovies { // TODO(gs) Can API_KEY be sent in header?
        val mostPopularMovies = "${baseUrl}discover/movie?api_key=${BuildConfig.apiKey}"
    }
}
