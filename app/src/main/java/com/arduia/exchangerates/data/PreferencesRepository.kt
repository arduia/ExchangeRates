package com.arduia.exchangerates.data

import com.arduia.exchangerates.domain.FlowResult
import com.arduia.exchangerates.domain.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aung Ye Htet at 18/01/2021 7:13 PM.
 */
interface PreferencesRepository {

    suspend fun setSelectedCurrencyType(value: String): Result<Boolean>

    fun getSelectedCurrencyTypeSync(): Result<String>

    fun getSelectedCurrencyTypeFlow(): FlowResult<String>

    suspend fun setLastSyncDate(value: Long): Result<Boolean>

    fun getLastSyncDateSync(): Result<Long>

    fun getLastSyncDateFlow(): FlowResult<Long>
}