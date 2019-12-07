package com.gurpreetsk.tvdb.base

interface BaseView<T> {
    fun render(state: T)
}
