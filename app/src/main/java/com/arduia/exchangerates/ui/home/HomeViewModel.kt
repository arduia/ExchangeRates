package com.arduia.exchangerates.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arduia.exchangerates.data.*
import com.arduia.exchangerates.data.exception.CacheException
import com.arduia.exchangerates.domain.*
import com.arduia.exchangerates.ui.common.*
import com.arduia.exchangerates.ui.home.format.DateFormatter
import com.arduia.exchangerates.ui.home.format.SyncDateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import javax.inject.Provider


/**
 * Cerated by Aung Ye Htet 16/01/2021 6:52 PM.
 */
class HomeViewModel @ViewModelInject constructor(
        exchangeRateMapperFactory: ExchangeRateMapperFactory,
        private val exchangeRatesRepository: ExchangeRatesRepository,
        private val currencyLayerRepository: CurrencyLayerRepository,
        private val rateConverter: ExchangeRateConverter,
        private val preferenceRepository: PreferencesRepository,
        private val dateFormatter: DateFormatter,
        private val currencyTypeMapper: Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel>,
        private val cacheSyncManager: CacheSyncManager
) : ViewModel() {

    private val _isSyncRunning = BaseLiveData<Boolean>()
    val isSyncRunning get() = _isSyncRunning.asLiveData()

    private val _lastUpdateDate = BaseLiveData<String>()
    val lastUpdateDate get() = _lastUpdateDate.asLiveData()

    private val _selectedCurrencyType = BaseLiveData<CurrencyTypeItemUiModel>()
    val selectedCurrencyType get() = _selectedCurrencyType.asLiveData()

    private val _onNoConnection = EventLiveData<Unit>()
    val onNoConnection get() = _onNoConnection.asLiveData()

    val currentRatePostfix
        get() = _enteredCurrency.asFlow()
                .combine(_selectedCurrencyType.asFlow()) { value, type ->
                    val amount = Amount.fromString(value)
                    "$amount ${type.currencyCode}"
                }.asLiveData()

    private val _enteredCurrency = BaseLiveData<String>()

    val exchangeRates = Transformations.switchMap(selectedCurrencyType) {
        createExchangeRatePagedListLiveData(it.currencyCode)
    }

    val isEmptyRates
        get() = exchangeRates.switchMap {
            BaseLiveData(it.isEmpty())
        }

    private val exchangeRateMapper =
            exchangeRateMapperFactory.create(rateConverter) {
                _selectedCurrencyType.value?.currencyCode ?: ""
            }

    init {
        observeSelectedCurrencyCode()
        observeLastUpdateDate()
        observeSyncState()
        syncData()
        _enteredCurrency post DEFAULT_ENTER_CURRENCY
    }

    private fun syncData() {
        viewModelScope.launch(Dispatchers.IO) {

            Timber.d("Sync")
            val result = cacheSyncManager.syncNow()
            if (result is ErrorResult) {
                val exception = result.exception
                if (exception is CacheException) {
                    Timber.d("cache Exception")

                }

            }
        }
    }

    private fun observeSyncState() {
        cacheSyncManager.progress
                .flowOn(Dispatchers.IO)
                .onEach {
                    Timber.d("state $it")
                    _isSyncRunning post (it != SyncState.Finished)
                }
                .launchIn(viewModelScope)
    }

    private fun observeSelectedCurrencyCode() {
        preferenceRepository.getSelectedCurrencyTypeFlow()
                .flowOn(Dispatchers.IO)
                .onSuccess {
                    Timber.d("getSelectedCurrencyType")
                    val type =
                            currencyLayerRepository.getCurrencyTypeByCurrencyCode(it).getDataOrError()
                    val rate = exchangeRatesRepository.getCurrencyRateByCurrencyCode(it).getDataOrError()

                    if (rate != null) {
                        rateConverter.setUSDRate(rate.exchangeRate)
                    }
                    if (type != null) {
                        _selectedCurrencyType post currencyTypeMapper.map(type)

                    } else {
                        _selectedCurrencyType post createEmptyCurrencyUiModel()
                    }
                }
                .onError {
                    _selectedCurrencyType post createEmptyCurrencyUiModel()
                }
                .launchIn(viewModelScope)
    }

    private fun createEmptyCurrencyUiModel() =
            CurrencyTypeItemUiModel(0, EMPTY_VALUE_TEXT, EMPTY_VALUE_TEXT)

    fun onEnterCurrencyValue(value: String) {

        val currencyValue = if (value.isEmpty()) DEFAULT_ENTER_CURRENCY else value

        _enteredCurrency post currencyValue
        val amount = Amount.fromString(currencyValue)
        rateConverter.setEnterdValue(amount)

        exchangeRates.value?.dataSource?.invalidate()
    }

    fun startSync() {
        cacheSyncManager.syncInBackground(viewModelScope, force = true)
    }

    private fun observeLastUpdateDate() {
        preferenceRepository.getLastSyncDateFlow()
                .flowOn(Dispatchers.IO)
                .onSuccess {
                    if (it == 0L) {
                        _lastUpdateDate post EMPTY_VALUE_TEXT
                        return@onSuccess
                    }
                    _lastUpdateDate post dateFormatter.format(it)
                }
                .launchIn(viewModelScope)
    }

    private fun createExchangeRatePagedListLiveData(selectedCurrencyCode: String): LiveData<PagedList<ExchangeRateItemUiModel>> {

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(50)
                .build()

        val dataSource = exchangeRatesRepository.getAllDataSource(selectedCurrencyCode)
                .map(exchangeRateMapper::map)

        return LivePagedListBuilder(dataSource, config).build()
    }

    companion object {
        private const val EMPTY_VALUE_TEXT = "---"
        private const val DEFAULT_ENTER_CURRENCY = "1.00"
    }
}