package com.example.twoscreens

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.atomic.AtomicBoolean

class Event<EVENT : Any> {

    private val channel = ConflatedBroadcastChannel<EVENT>()

    private val isEventConsumed = AtomicBoolean(false)

    fun postEvent(event: EVENT) {
        isEventConsumed.set(false)
        channel.offer(event)
    }

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
