package com.example.twoscreens.ui.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
open class StateStore<STATE : Any>(initial: STATE) {

    private val channel = ConflatedBroadcastChannel(initial)

    fun setState(setter: STATE.() -> STATE) {
        setStateAndReturn(setter)
    }

    open fun setStateAndReturn(setter: STATE.() -> STATE): STATE {
        val previous = channel.value
        val new = setter(previous)
        if (new != previous) {
            channel.offer(new)
        }
        return new
    }

    @FlowPreview
    fun observe() = channel.asFlow()

    @FlowPreview
    val currentState get() = runBlocking { observe().first() }
}
