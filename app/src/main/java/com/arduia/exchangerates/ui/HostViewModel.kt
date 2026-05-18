package com.arduia.exchangerates.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arduia.exchangerates.data.CacheSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 16/01/2021 6:53 PM.
 */
@HiltViewModel
class HostViewModel @Inject constructor(
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