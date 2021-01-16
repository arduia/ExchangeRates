package com.arduia.exchangerates.ui.common

import androidx.lifecycle.Observer

/**
 * Inspired by Google
 * Event State with data that can be consumed once.
 */
open class Event<out T> (private val content: T){

    var hasHandled = false
        private set

    fun getContentIfNotHandled(): T?{
        return when (hasHandled){
            true -> null
            false -> {
                hasHandled = true
                content
            }
        }
    }
    fun peekContent(): T = content
}

/**
 * Observer that consume {@link Event} Data
 */
class EventObserver<T> (private val onEventUnhandledContent: (T) -> Unit): Observer<Event<T>>{

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

/**
 * Return New Unit Event for State Representation.
 */
val UnitEvent get() =  Event(Unit)

/**
 * New Event with content{T}
 */
fun <T>event(content: T) = Event(content)