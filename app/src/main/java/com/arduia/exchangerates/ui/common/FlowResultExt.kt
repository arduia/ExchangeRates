package com.arduia.exchangerates.ui.common

import androidx.lifecycle.MutableLiveData
import com.arduia.exchangerates.domain.Result


inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Result.Success) {
        liveData.value = data
    }
}