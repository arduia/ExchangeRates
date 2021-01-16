package com.arduia.exchangerates.ui.currencies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.ui.common.BaseLiveData
import com.arduia.exchangerates.ui.common.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:53 PM.
 */
class ChooseCurrencyViewModel @ViewModelInject constructor() : ViewModel() {

    private val _isCurrenciesDownloading = BaseLiveData<Boolean>()
    val isCurrenciesDownloading get() = _isCurrenciesDownloading

    private val _isEmptyCurrencies = BaseLiveData<Boolean>()
    val isEmptyCurrencies get() = _isEmptyCurrencies.asLiveData()

    init {
        startFakeDownload()
    }

    private fun startFakeDownload(){
        viewModelScope.launch(Dispatchers.IO){
            _isEmptyCurrencies post false
            delay(1000)
            _isCurrenciesDownloading post true
            delay(3000)
            _isCurrenciesDownloading post false
            _isEmptyCurrencies post true
        }
    }

}