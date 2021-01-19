package com.arduia.exchangerates.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.data.*
import com.arduia.exchangerates.domain.getDataOrError
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:51 PM.
 */
class SplashViewModel @ViewModelInject constructor(
        private val cacheSyncManager: CacheSyncManager,
        private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _isLoading = BaseLiveData<Boolean>()
    val isLoading get() = _isLoading.asLiveData()

    private val _onSplashFinished = EventLiveData<Unit>()
    val onSplashFinished get() = _onSplashFinished.asLiveData()

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(SPLASH_INITIAL_SHOW_TIME)
            _isLoading post true
            cacheSyncManager.syncNow()
            val selectedType = preferencesRepository.getSelectedCurrencyTypeSync().getDataOrError()
            if (selectedType.isEmpty()) {
                preferencesRepository.setSelectedCurrencyType("USD")
            }
            _isLoading post false
            _onSplashFinished post UnitEvent
        }
    }

    companion object{
        private const val SPLASH_INITIAL_SHOW_TIME = 500L
    }

}