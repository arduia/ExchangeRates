package com.arduia.exchangerates.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Base MutableLiveData that has LiveData casting with
 * fun asLiveData() to use for encapsulations such as:
 *
 * Example -
 * private val _data = BaseLiveData<String>();
 * val data = _data.asLiveData()
 */

class BaseLiveData<T>(initValue: T? = null) : MutableLiveData<T>() {
    init {
        if (initValue != null) {
            this.value = initValue
        }
    }

    fun asLiveData(): LiveData<T> = this
}

typealias EventLiveData<T> = BaseLiveData<Event<T>>

infix fun <T> MutableLiveData<T>.set(value: T) {
    setValue(value)
}

infix fun <T> MutableLiveData<T>.post(value: T) {
    postValue(value)
}