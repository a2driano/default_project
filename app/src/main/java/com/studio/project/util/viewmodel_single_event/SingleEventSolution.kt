package com.studio.project.util.viewmodel_single_event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Dmitry Torin on 2020-01-13.
 * mova.io
 * Slack: @dt
 */

class LiveEvent : MutableLiveData<Event<Boolean>>() {
    fun produce() {
        value = Event(true)
    }
}

class LongLiveEvent : MutableLiveData<Event<Long>>() {
    fun produce(num: Long) {
        value = Event(num)
    }
}

class IntLiveEvent : MutableLiveData<Event<Int>>() {
    fun produce(num: Int) {
        value = Event(num)
    }
}

class StringLiveEvent : MutableLiveData<Event<String>>() {
    fun produce(str: String) {
        value = Event(str)
    }
}


open class Event<out T>(private val content: T) {
    var wasConsumed = false
        private set

    fun consumeIfCan(): T? {
        return if (wasConsumed) {
            null

        } else {
            wasConsumed = true
            content
        }
    }

    fun consumeWhatever(): T = content
}

fun MutableLiveData<Event<Boolean>>.produce() {
    value = Event(true)
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.consumeIfCan()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}