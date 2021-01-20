package com.arduia.exchangerates.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.data.CacheSyncManager
import timber.log.Timber

/**
 * Created by Aung Ye Htet at 16/01/2021 6:53 PM.
 */
class HostViewModel @ViewModelInject constructor(
        private val syncManager: CacheSyncManager
) : ViewModel() {

    init {
        syncManager.setAutoRefresh(scope = viewModelScope) //Register Auto Sync Scope
    }

    override fun onCleared() {
        super.onCleared()
        syncManager.setAutoRefresh(null)  //Unregister Auto Sync
    }
}