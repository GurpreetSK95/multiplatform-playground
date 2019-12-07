package com.gurpreetsk.tvdb.shared

import kotlinx.coroutines.Dispatchers

internal class AndroidSchedulers: Schedulers {
    override fun io() = Dispatchers.IO
    override fun main() = Dispatchers.Main
    override fun computation() = Dispatchers.Default
}
