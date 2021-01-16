package com.arduia.exchangerates.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal


/**
 * Cerated by Aung Ye Htet 16/01/2021 6:52 PM.
 */
class HomeViewModel @ViewModelInject constructor() : ViewModel() {

    private val _isSyncRunning = BaseLiveData<Boolean>()
    val isSyncRunning get() = _isSyncRunning.asLiveData()

    private val _lastUpdateDate = BaseLiveData<String>()
    val lastUpdateDate get() = _lastUpdateDate.asLiveData()

    private val _selectedCurrencyType = BaseLiveData<CurrencyTypeItemUiModel>()
    val selectedCurrencyType get() = _selectedCurrencyType.asLiveData()

    private val _enteredCurrencyValue = BaseLiveData<BigDecimal>()
    val enteredCurrencyValue get() = _enteredCurrencyValue.asLiveData()

    private val _isEmptyRates = BaseLiveData<Boolean>()
    val isEmptyRates get() = _isEmptyRates.asLiveData()

    private val _isRatesDownloading = BaseLiveData<Boolean>()
    val isRatesDownloading get() = _isRatesDownloading.asLiveData()

    private val _onNoConnection = EventLiveData<Unit>()
    val onNoConnection get() = _onNoConnection.asLiveData()

    init {
        showFakeCurrencyType()
    }

    fun startSync(){
        startFakeSync()
    }

    private fun showFakeCurrencyType(){
        _selectedCurrencyType post CurrencyTypeItemUiModel(0, "MMK", "Myanmar")
    }

    private fun startFakeSync(){
        viewModelScope.launch(Dispatchers.IO){
            _isEmptyRates post false
            _isSyncRunning post true
            delay(3000)
            _isSyncRunning post false
            delay(500)
            _isRatesDownloading post true
            delay(2000)
            _isRatesDownloading post false
            _isEmptyRates post true
            onEmptyData()
            delay(1000)
            _onNoConnection post UnitEvent
        }
    }

    private fun onEmptyData(){
        _selectedCurrencyType post getEmptyCurrencyType()
        _lastUpdateDate post "- - -"
    }

    private fun getEmptyCurrencyType() =
            CurrencyTypeItemUiModel(0, "---", "---")


}