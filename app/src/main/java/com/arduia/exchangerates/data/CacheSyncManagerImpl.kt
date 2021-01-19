package com.arduia.exchangerates.data

import com.arduia.exchangerates.data.exception.CacheException
import com.arduia.exchangerates.data.exception.ServerErrorException
import com.arduia.exchangerates.domain.ErrorResult
import com.arduia.exchangerates.domain.Result
import com.arduia.exchangerates.domain.SuccessResult
import com.arduia.exchangerates.domain.getDataOrError
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Aung Ye Htet at 18/01/2021 7:33 PM.
 */
@Singleton
class CacheSyncManagerImpl @Inject constructor(
        private val preferencesRepository: PreferencesRepository,
        private val currencyLayerRepository: CurrencyLayerRepository
) : CacheSyncManager {

    private val progressChannel = ConflatedBroadcastChannel<SyncState>()

    override val progress: Flow<SyncState> = progressChannel.asFlow()

    private var currentJob: Job? = null
    private var currentTimer: Timer? = null
    private var autoSyncCoroutineScope: CoroutineScope? = null

    init {
        progressChannel.offer(SyncState.Finished)
    }

    override suspend fun syncNow(force: Boolean): Result<SyncState.Finished> {
        Timber.d("syncNow $force")
        try {
            progressChannel.offer(SyncState.Initial)
            if (isOverMinimumRefreshInterval() || force){
                return startSyncProgress()
            }
            progressChannel.offer(SyncState.Finished)
            return SuccessResult(SyncState.Finished)

        } catch (e: Exception) {
            progressChannel.offer(SyncState.Finished)
            return cacheErrorResult(e)
        }
    }

    private suspend fun startSyncProgress(): Result<SyncState.Finished> {


        progressChannel.offer(SyncState.CurrenciesDownloading)
        val nameResponse = currencyLayerRepository.getDownloadCurrencyNames().getDataOrError()

        if (nameResponse.success.not()) throw ServerErrorException(nameResponse.errorInfo!!.code, nameResponse.errorInfo.info)

        progressChannel.offer(SyncState.ExchangeRateDownloading)
        val rateResponse = currencyLayerRepository.getDownloadedUSDCurrencyRates().getDataOrError()
        if (rateResponse.success.not()) throw ServerErrorException(rateResponse.error!!.code, rateResponse.error.info)

        val rates = rateResponse.exchangeRate?.map {
            CacheExchangeRateDto(0, it.code, it.rate)
        } ?: throw ServerErrorException(404, "Rate not found!")

        val names = nameResponse.currencyList?.map {
            CurrencyTypeDto(0, it.code, it.name)
        } ?: throw ServerErrorException(404, "Name not found!")

        currencyLayerRepository.insertAllCacheExchangeRate(rates)
        currencyLayerRepository.insertAllCurrencyType(names)

        val currentDate = Date().time
        preferencesRepository.setLastSyncDate(currentDate)
        updateAutoRefreshTimer(currentDate)

        progressChannel.offer(SyncState.Finished)
        return SuccessResult(SyncState.Finished)
    }

    private suspend fun isOverMinimumRefreshInterval(): Boolean {
        val currentDate = Date().time
        val syncDate = preferencesRepository.getLastSyncDateSync().getDataOrError()
        return (currentDate - syncDate) >= REFRESH_INTERVAL
    }

    override fun syncInBackground(scope: CoroutineScope, force: Boolean) {
        currentJob?.cancel()
        currentJob = scope.launch(Dispatchers.IO) {
            syncNow(force)
        }
    }

    override fun setAutoRefresh(scope: CoroutineScope?) {
        this.autoSyncCoroutineScope = scope
        if (scope == null) return
        scope.launch(Dispatchers.IO) {
            val time = preferencesRepository.getLastSyncDateSync().getDataOrError()
            updateAutoRefreshTimer(time)
        }
    }

    private fun updateAutoRefreshTimer(lastSyncDate: Long) {
        val nextSyncDate = Date().apply {
            time = lastSyncDate + REFRESH_INTERVAL
        }
        currentTimer?.cancel()
        currentTimer = Timer()
        currentTimer?.schedule(
                object : TimerTask() {
                    override fun run() {
                        val scope = autoSyncCoroutineScope ?: return
                        syncInBackground(scope)
                    }
                },
                nextSyncDate,
                REFRESH_INTERVAL
        )
    }

    companion object {
        private const val REFRESH_INTERVAL =  1860_000L //ms =  31min
    }
}

private fun cacheErrorResult(e: Exception) = ErrorResult(CacheException(cause = e))