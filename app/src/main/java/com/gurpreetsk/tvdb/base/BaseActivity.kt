package com.gurpreetsk.tvdb.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gurpreetsk.tvdb.LifecycleEvent
import com.gurpreetsk.tvdb.LifecycleEvent.*
import com.gurpreetsk.tvdb.shared.AndroidSchedulers
import com.gurpreetsk.tvdb.shared.Restorable
import com.gurpreetsk.tvdb.shared.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

private const val LAST_KNOWN_STATE = "LAST_KNOWN_STATE"

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseActivity<T : Restorable> : AppCompatActivity(), BaseView<T> {
    private val job by lazy { Job() }
    private val scope by lazy {
        object : CoroutineScope {
            override val coroutineContext: CoroutineContext
                get() = job + schedulers.main()
        }
    }

    private val lifecycle = ConflatedBroadcastChannel<LifecycleEvent>()
    private val states = ConflatedBroadcastChannel<T>()

    private lateinit var lifecycleEvent: LifecycleEvent
    private var lastKnownState: T? = null

    protected val schedulers: Schedulers by lazy { AndroidSchedulers() }

    open fun setupUi() {}

    abstract fun getLayoutRes(): ViewGroup

    abstract fun bind(states: Flow<T>): Flow<T>

    open fun preBind() {}

    open fun postBind() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        if (savedInstanceState == null) lifecycleEvent = CREATED else RESTORED

        setupUi()
    }

    override fun onStart() {
        super.onStart()

        preBind()

        scope.launch(schedulers.io()) {
            bind(states.asFlow())
                .flowOn(schedulers.io())
                .collect { state ->
                    states.send(state)

                    withContext(schedulers.main()) {
                        render(state)
                        lastKnownState = state
                    }
                }
        }

        scope.launch(schedulers.io()) {
            lifecycle.send(lifecycleEvent)
        }

        postBind()
    }

    // TODO(gs) think of a better name
    protected fun flowLifecycle(): Flow<LifecycleEvent> =
        lifecycle.asFlow()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(LAST_KNOWN_STATE, lastKnownState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<T>(LAST_KNOWN_STATE)?.let {
            lastKnownState = it
            lifecycleEvent = RESTORED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleEvent = STOPPED
        scope.cancel()
    }
}
