package com.arduia.exchangerates.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:51 PM.
 */
class SplashViewModel @ViewModelInject constructor() : ViewModel() {

    private val _isLoading = BaseLiveData<Boolean>()
    val isLoading get() = _isLoading.asLiveData()

    private val _onSplashFinished = EventLiveData<Unit>()
    val onSplashFinished get() = _onSplashFinished.asLiveData()

    init {
        showFakeLoading()
    }

    private fun showFakeLoading() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _isLoading post true
            delay(3000)
            _isLoading post false
            delay(1000)
            _onSplashFinished post UnitEvent
        }
    }

}