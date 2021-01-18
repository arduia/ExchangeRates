package com.arduia.exchangerates.ui.currencies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arduia.exchangerates.data.CurrencyLayerRepository
import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.data.PreferencesRepository
import com.arduia.exchangerates.domain.Mapper
import com.arduia.exchangerates.domain.SuccessResult
import com.arduia.exchangerates.domain.getDataOrError
import com.arduia.exchangerates.ui.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Cerated by Aung Ye Htet 16/01/2021 6:53 PM.
 */
class ChooseCurrencyViewModel @ViewModelInject constructor(
    private val currencyMapper: Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel>,
    private val currencyLayerRepository: CurrencyLayerRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _isCurrenciesDownloading = BaseLiveData<Boolean>()
    val isCurrenciesDownloading get() = _isCurrenciesDownloading

    private val _onItemSelected = EventLiveData<Unit>()
    val onItemSelected get() = _onItemSelected.asLiveData()

    private val _onItemSelectError = EventLiveData<Unit>()
    val onItemSelectError get() = _onItemSelectError.asLiveData()

    val currencyTypeList = createLivePagedList()

    val isEmptyCurrencies
        get() = Transformations.switchMap(currencyTypeList) {
            BaseLiveData(it.isEmpty())
        }

    fun onSelect(type: CurrencyTypeItemUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val successResult = preferencesRepository.setSelectedCurrencyType(type.currencyCode)

            if (successResult !is SuccessResult) {
                return@launch
            }

            if (successResult.data) {
                _onItemSelected post UnitEvent
            } else {
                _onItemSelectError post UnitEvent
            }

        }
    }

    private fun createLivePagedList(): LiveData<PagedList<CurrencyTypeItemUiModel>> {
        val config = PagedList.Config.Builder()
            .setPageSize(50)
            .setInitialLoadSizeHint(10)
            .build()

        val dataSource = currencyLayerRepository.getAllCurrencyTypeDataSource()
            .map(currencyMapper::map)

        return LivePagedListBuilder(dataSource, config).build()
    }


}