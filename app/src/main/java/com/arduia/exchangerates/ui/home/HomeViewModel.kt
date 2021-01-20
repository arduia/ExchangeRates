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
import timber.log.Timber
import java.util.*


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

    private val _onNoConnection = EventLiveData<Boolean>()
    val onNoConnection get() = _onNoConnection.asLiveData()

    private val _onServerError = EventLiveData<Unit>()
    val onServerError get() = _onServerError.asLiveData()

    private val _onToast = EventLiveData<String>()
    val onToast get() = _onToast.asLiveData()

    val currentRatePostfix
        get() = _enteredCurrency.asFlow()
                .combine(_selectedCurrencyType.asFlow()) { value, type ->
                    val amount = Amount.fromString(value)
                    "$amount ${type.currencyCode}"
                }.asLiveData()

    private val _enteredCurrency = BaseLiveData(initValue = DEFAULT_ENTER_CURRENCY)

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
        syncInBackground(force = false)
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
                            currencyLayerRepository.getCurrencyTypeByCurrencyCode(it).getDataOrThrow()
                    val rate = exchangeRatesRepository.getCurrencyRateByCurrencyCode(it).getDataOrThrow()

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

    private fun syncInBackground(force: Boolean) {
        cacheSyncManager.syncInBackground(viewModelScope, force = force) { result ->
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
    }

    fun startSync() {
        syncInBackground(true)
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