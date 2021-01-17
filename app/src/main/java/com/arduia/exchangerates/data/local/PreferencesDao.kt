package com.arduia.exchangerates.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Created by Aung Ye Htet at 17/01/2021 10:45 AM.
 */
interface PreferencesDao {

    suspend fun setSelectedCurrencyType(value: String): Boolean

    //Sync Postfix => Get Result Now Synchronously
    fun getSelectedCurrencyTypeSync(): String

    //Flow Postfix => Get Result Asynchronously, may be later
    fun getSelectedCurrencyTypeFlow(): Flow<String>

    suspend fun setLastSyncDate(value: Long): Boolean

    fun getLastSyncDateSync(): Long

}