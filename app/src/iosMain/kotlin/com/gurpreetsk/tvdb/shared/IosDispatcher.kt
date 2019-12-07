package com.gurpreetsk.tvdb.shared

import kotlinx.coroutines.CoroutineDispatcher

class IosDispatcher : Schedulers {
    override fun io(): CoroutineDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun main(): CoroutineDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun computation(): CoroutineDispatcher {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
