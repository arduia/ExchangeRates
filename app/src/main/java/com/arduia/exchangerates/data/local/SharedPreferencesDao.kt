package com.arduia.exchangerates.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 17/01/2021 10:55 AM.
 */
class SharedPreferencesDao @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesDao {

    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val selectedCurrencyTypeChannel = MutableStateFlow<String>("")
    private val lastSyncDateChannel = MutableStateFlow<Long>(0L)

    override suspend fun setSelectedCurrencyType(value: String): Boolean {
        preference.edit()
            .putString(KEY_SELECTED_CURRENCY_TYPE, value)
            .apply()
        //Fetch Current Value.
        val currentValue = getSelectedCurrencyTypeSync()
        //Update Listeners
        selectedCurrencyTypeChannel.value = currentValue
        //Check it successfully inserted or not.
        return currentValue == value
    }

    override suspend fun getSelectedCurrencyTypeSync(): String {
        return preference.getString(KEY_SELECTED_CURRENCY_TYPE, DEFAULT_SELECTED_CURRENCY_TYPE)
            ?: DEFAULT_SELECTED_CURRENCY_TYPE
    }

    override fun getSelectedCurrencyTypeFlow(): Flow<String> {
        runBlocking {
            val currentValue = getSelectedCurrencyTypeSync()
            selectedCurrencyTypeChannel.value = currentValue
        }
        return selectedCurrencyTypeChannel.asStateFlow()
    }

    override suspend fun setLastSyncDate(value: Long): Boolean {
        preference.edit()
            .putLong(KEY_LAST_SYNC_DATE, value)
            .apply()
        val currentValue = getLastSyncDateSync()
        lastSyncDateChannel.value = currentValue
        return currentValue == value
    }

    override suspend fun getLastSyncDateSync(): Long {
        return preference.getLong(KEY_LAST_SYNC_DATE, DEFAULT_LAST_SYNC_DATE)
    }

    override fun getLastSyncDateFlow(): Flow<Long> {
        runBlocking {
            val currentValue = getLastSyncDateSync()
            lastSyncDateChannel.value = currentValue
        }
        return lastSyncDateChannel.asStateFlow()
    }

    companion object {
        private const val PREFERENCE_NAME = "Exchange_Rate_State"

        private const val KEY_LAST_SYNC_DATE = "last_sync_date"
        private const val DEFAULT_LAST_SYNC_DATE = 0L

        private const val KEY_SELECTED_CURRENCY_TYPE = "selected_currency_type"
        private const val DEFAULT_SELECTED_CURRENCY_TYPE = ""
    }
}