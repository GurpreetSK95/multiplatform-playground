package com.gurpreetsk.tvdb.shared

import io.ktor.client.HttpClient

expect object HttpClient {
    fun get(): HttpClient
}
