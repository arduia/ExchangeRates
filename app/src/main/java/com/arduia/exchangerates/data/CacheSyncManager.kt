package com.arduia.exchangerates.data

import com.arduia.exchangerates.domain.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aung Ye Htet at 18/01/2021 7:03 PM.
 */
interface CacheSyncManager {

    val progress: Flow<SyncState>

    suspend fun syncNow(force: Boolean = false): Result<SyncState.Finished>

    fun syncInBackground(scope: CoroutineScope, force: Boolean = false, onFinished: (Result<SyncState.Finished>) -> Unit = {})

    fun setAutoRefresh(scope: CoroutineScope?)

}

sealed class SyncState {
    object CurrenciesDownloading : SyncState()
    object ExchangeRateDownloading : SyncState()
    object Finished : SyncState()
    object Initial : SyncState()
}

// Progress Flow: Finished -> Initial -> CurrenciesDownloading -> ExchangeRateDownloading -> Finished