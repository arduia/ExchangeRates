package com.arduia.exchangerates.data

import com.arduia.exchangerates.domain.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 18/01/2021 7:33 PM.
 */
class CacheSyncManagerImpl @Inject constructor(): CacheSyncManager {

    private val progressChannel = ConflatedBroadcastChannel<SyncState>()

    override val progress: Flow<SyncState> = progressChannel.asFlow()

    override suspend fun syncNow(): Result<SyncState.Finished> {
        TODO("Not yet implemented")
    }

    override fun syncInBackground(coroutineScope: CoroutineScope) {
        coroutineScope.launch {

        }
    }

}