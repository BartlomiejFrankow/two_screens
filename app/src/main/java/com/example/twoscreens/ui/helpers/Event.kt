package com.example.twoscreens.ui.helpers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.atomic.AtomicBoolean


class Event<EVENT : Any> {

    @ExperimentalCoroutinesApi
    private val channel = ConflatedBroadcastChannel<EVENT>()

    private val isEventConsumed = AtomicBoolean(false)

    @ExperimentalCoroutinesApi
    fun postEvent(event: EVENT) {
        isEventConsumed.set(false)
        channel.offer(event)
    }

    @FlowPreview
    fun onEachEvent(launchWhenStarted: (suspend () -> Unit) -> Unit, observer: (EVENT) -> Unit) {
        val flow = channel.asFlow()
        launchWhenStarted {
            flow.collect { event ->
                if (!isEventConsumed.getAndSet(true)) {
                    observer(event)
                }
            }
        }
    }


    // Use it only for the tests.
    val lastValue: EVENT
        get() = channel.value

}
