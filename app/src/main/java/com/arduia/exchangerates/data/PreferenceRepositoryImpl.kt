package com.arduia.exchangerates.data

import com.arduia.exchangerates.data.ext.asResultFlow
import com.arduia.exchangerates.data.ext.result
import com.arduia.exchangerates.data.local.PreferencesDao
import com.arduia.exchangerates.domain.*
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Aung Ye Htet at 18/01/2021 7:53 PM.
 */
class PreferenceRepositoryImpl @Inject constructor(
        private val dao: PreferencesDao
) : PreferencesRepository {

    override suspend fun setSelectedCurrencyType(value: String): Result<Boolean> {
        return result { dao.setSelectedCurrencyType(value) }
    }

    override fun getSelectedCurrencyTypeSync(): Result<String> {
        return result { dao.getSelectedCurrencyTypeSync() }
    }

    override fun getSelectedCurrencyTypeFlow(): FlowResult<String> {
        return dao.getSelectedCurrencyTypeFlow().asResultFlow()
    }

    override suspend fun setLastSyncDate(value: Long): Result<Boolean> {
        return result { dao.setLastSyncDate(value) }
    }

    override fun getLastSyncDateSync(): Result<Long> {
        return result { dao.getLastSyncDateSync() }
    }

    override fun getLastSyncDateFlow(): FlowResult<Long> {
        return dao.getLastSyncDateFlow().asResultFlow()
    }
}