package com.arduia.exchangerates.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.data.*
import com.arduia.exchangerates.domain.getDataOrError
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:51 PM.
 */
class SplashViewModel @ViewModelInject constructor(
    private val currencyLayerRepository: CurrencyLayerRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _isLoading = BaseLiveData<Boolean>()
    val isLoading get() = _isLoading.asLiveData()

    private val _onSplashFinished = EventLiveData<Unit>()
    val onSplashFinished get() = _onSplashFinished.asLiveData()

    init {
        showFakeLoading()
    }

    private fun showFakeLoading() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading post true

            val nameResponse = currencyLayerRepository.getDownloadCurrencyNames().getDataOrError()
            val rateResponse =
                currencyLayerRepository.getDownloadedUSDCurrencyRates().getDataOrError()

            currencyLayerRepository.deleteAllCurrencyType()
            currencyLayerRepository.deleteAllCacheExchangeRate()

            nameResponse.currencyList?.map {
                CurrencyTypeDto(0, it.code, it.name)
            }?.also {
                currencyLayerRepository.insertAllCurrencyType(it)
                Timber.d("onCurrencyType Inserted!")
            }

            rateResponse.exchangeRate?.map {
                CacheExchangeRateDto(0, it.code, it.rate)
            }?.also {
                currencyLayerRepository.insertAllCacheExchangeRate(it)
                Timber.d("onExchangeRate Success")
            }

            val selectedType = preferencesRepository.getSelectedCurrencyTypeSync().getDataOrError()
            if(selectedType.isEmpty()){
                preferencesRepository.setSelectedCurrencyType("USD")
                Timber.d("SetDefault as USD")
            }
            _isLoading post false
            _onSplashFinished post UnitEvent
        }
    }

}