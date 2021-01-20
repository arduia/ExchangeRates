package com.arduia.exchangerates.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arduia.exchangerates.data.*
import com.arduia.exchangerates.data.exception.NoConnectionException
import com.arduia.exchangerates.data.exception.NoInternetException
import com.arduia.exchangerates.data.exception.ServerErrorException
import com.arduia.exchangerates.domain.*
import com.arduia.exchangerates.ui.common.*
import com.arduia.exchangerates.ui.home.format.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*


/**
 * Cerated by Aung Ye Htet 16/01/2021 6:52 PM.
 */
class HomeViewModel @ViewModelInject constructor(
        exchangeRateMapperFactory: ExchangeRateMapperFactory,
        private val exchangeRatesRepository: ExchangeRatesRepository,
        private val currencyLayerRepository: CurrencyLayerRepository,
        private val rateConverter: ExchangeRateConverter,
        private val preferencesRepository: PreferencesRepository,
        private val dateFormatter: DateFormatter,
        private val currencyTypeMapper: Mapper<CurrencyTypeDto, CurrencyTypeItemUiModel>,
        private val cacheSyncManager: CacheSyncManager
) : ViewModel() {

    private val _isSyncRunning = BaseLiveData<Boolean>()
    val isSyncRunning get() = _isSyncRunning.asLiveData()

    private val _lastUpdatedDate = BaseLiveData<String>()
    val lastUpdatedDate get() = _lastUpdatedDate.asLiveData()

    private val _selectedCurrencyType = BaseLiveData<CurrencyTypeItemUiModel>()
    val selectedCurrencyType get() = _selectedCurrencyType.asLiveData()

    private val _onNoConnection = EventLiveData<Boolean>()
    val onNoConnection get() = _onNoConnection.asLiveData()

    private val _onServerError = EventLiveData<Unit>()
    val onServerError get() = _onServerError.asLiveData()

    val currentRatePostfix get() = createRatePostfixLiveData()

    private val _enteredCurrency = BaseLiveData(initValue = DEFAULT_ENTER_CURRENCY) //For internal data flow,no for Fragment to observe

    val exchangeRates = selectedCurrencyType.switchMap {
        createExchangeRatePagedListLiveData(it.currencyCode)
    }

    val isExchangeRatesEmpty = exchangeRates.switchMap {
        BaseLiveData(it.isEmpty())
    }

    private val exchangeRateMapper = exchangeRateMapperFactory.create(rateConverter) {
        _selectedCurrencyType.value?.currencyCode ?: "" //Provide<String> selectedCurrencyType
    }

    init {
        observeSelectedCurrencyCode()
        observeLastUpdateDate()
        observeSyncState()
        syncInBackground(force = false) //Sync again for home to recover Splash sync failure
    }

    fun startSync() {
        syncInBackground(force = true)
    }

    fun onEnterCurrencyValue(value: String) {
        val currencyValue = if (value.isEmpty()) DEFAULT_ENTER_CURRENCY else value
        _enteredCurrency set currencyValue
        updateExchangeRateConverter(currencyValue)
    }

    private fun updateExchangeRateConverter(value: String) {
        val amount = Amount.fromString(value)
        rateConverter.setEnterdValue(amount)
        exchangeRates.value?.dataSource?.invalidate()
    }

    private fun observeSyncState() {
        cacheSyncManager.progress
                .flowOn(Dispatchers.IO)
                .onEach {
                    _isSyncRunning post (it != SyncState.Finished) //for sync states => Initial, Downloading
                }
                .launchIn(viewModelScope)
    }

    private fun observeSelectedCurrencyCode() {
        preferencesRepository.getSelectedCurrencyTypeFlow()
                .flowOn(Dispatchers.IO)
                .onSuccess {
                    val type =
                            currencyLayerRepository.getCurrencyTypeByCurrencyCode(it).getDataOrThrow()
                    val rate = exchangeRatesRepository.getCurrencyRateByCurrencyCode(it).getDataOrThrow()

                    if (rate != null) {
                        rateConverter.setUSDRate(rate.exchangeRate) //config for current currency type
                    }

                    when (type != null) {
                        true -> _selectedCurrencyType post currencyTypeMapper.map(type)
                        false -> _selectedCurrencyType post createEmptyCurrencyUiModel() //Empty Currency Type
                    }
                }
                .onError {
                    _selectedCurrencyType post createEmptyCurrencyUiModel()
                }
                .launchIn(viewModelScope)
    }

    private fun createEmptyCurrencyUiModel() =
            CurrencyTypeItemUiModel(0, EMPTY_VALUE_TEXT, EMPTY_VALUE_TEXT)

    private fun syncInBackground(force: Boolean) { //force => should sync although date is not over minimum refresh interval
        cacheSyncManager.syncInBackground(viewModelScope, force = force) { result ->
            onSyncResult(result)
        }
    }

    private fun onSyncResult(result: Result<SyncState>) {
        if (result is ErrorResult) {
            when (result.exception) {
                is NoInternetException, is NoConnectionException -> {
                    val shouldForceShow = exchangeRates.value?.isEmpty() ?: true
                    _onNoConnection post event(shouldForceShow)
                }
                is ServerErrorException -> {
                    _onServerError post UnitEvent
                }
            }
        }
    }

    private fun observeLastUpdateDate() {
        preferencesRepository.getLastSyncDateFlow()
                .flowOn(Dispatchers.IO)
                .onSuccess {
                    if (it == 0L) {
                        _lastUpdatedDate post EMPTY_VALUE_TEXT
                        return@onSuccess
                    }
                    _lastUpdatedDate post dateFormatter.format(it)
                }
                .launchIn(viewModelScope)
    }

    private fun createRatePostfixLiveData() = _enteredCurrency.asFlow()
            .combine(_selectedCurrencyType.asFlow()) { value, type ->
                val amount = Amount.fromString(value)
                "$amount ${type.currencyCode}"
            }.asLiveData()

    private fun createExchangeRatePagedListLiveData(selectedCurrencyCode: String):
            LiveData<PagedList<ExchangeRateItemUiModel>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(40)
                .setPageSize(20)
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