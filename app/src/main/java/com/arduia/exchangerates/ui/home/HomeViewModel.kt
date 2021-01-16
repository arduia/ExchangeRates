package com.arduia.exchangerates.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.arduia.exchangerates.ui.common.BaseLiveData
import com.arduia.exchangerates.ui.common.CurrencyTypeItemUiModel
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


}