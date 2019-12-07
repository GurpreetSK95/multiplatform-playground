package com.gurpreetsk.tvdb.data

data class FetchEvent<out T>(
    val action: FetchAction,
    val result: T?,
    val errors: List<ApplicationError>? = null
)
