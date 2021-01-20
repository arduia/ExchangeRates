package com.arduia.exchangerates.ui.currencies

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.data.PreferencesRepository
import com.arduia.exchangerates.domain.Mapper
import com.arduia.exchangerates.domain.SuccessResult
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:53 PM.
 */
class ChooseCurrencyViewModel @ViewModelInject constructor(
        private val currencyMapper: Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel>,
        private val currencyDataSourceFactory: CurrencyDataSourceFactory,
        private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _onCurrencySelected = EventLiveData<Unit>()
    val onCurrencySelected get() = _onCurrencySelected.asLiveData()

    private val _onCurrencySelectError = EventLiveData<Unit>()
    val onCurrencySelectError get() = _onCurrencySelectError.asLiveData()

    val currencyTypeList = createLivePagedList()

    private val queryText = BaseLiveData("")

    val isEmptyCurrencies = queryText.asFlow()
            .combine(currencyTypeList.asFlow()) { query, list ->
                query.isEmpty() && list.isEmpty() //Query and CurrencyList is Empty => Empty Currencies
            }.asLiveData()

    fun onCurrencySelected(type: CurrencyTypeItemUiModel) {
        saveCurrencyCode(type.currencyCode)
    }

    private fun saveCurrencyCode(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = preferencesRepository.setSelectedCurrencyType(code)
            if (result !is SuccessResult) { //on Error Result
                _onCurrencySelectError post UnitEvent
                return@launch
            }
            when (result.data) {
                true -> _onCurrencySelected post UnitEvent //Successful
                false -> _onCurrencySelectError post UnitEvent //Failure
            }
        }
    }

    fun onQuery(query: String) {
        queryText post query
        updateDataSource(query)
    }

    private fun updateDataSource(query: String) {
        currencyDataSourceFactory.setQuery(query)
        currencyTypeList.value?.dataSource?.invalidate()
    }

    private fun createLivePagedList(): LiveData<PagedList<CurrencyTypeItemUiModel>> {
        val config = PagedList.Config.Builder()
                .setPageSize(50)
                .setInitialLoadSizeHint(10)
                .build()
        val dataSource = currencyDataSourceFactory.map(currencyMapper::map)
        return LivePagedListBuilder(dataSource, config).build()
    }

}