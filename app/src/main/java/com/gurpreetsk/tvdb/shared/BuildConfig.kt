package com.gurpreetsk.tvdb.shared

import com.gurpreetsk.tvdb.BuildConfig

actual object BuildConfig {
    actual val apiKey: String
        get() = BuildConfig.API_KEY
}
