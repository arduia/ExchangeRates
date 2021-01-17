package com.arduia.exchangerates

import android.app.Application
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Aung Ye Htet at 16/1/2021 5:55 PM.
 */
@HiltAndroidApp
class ExchangeRatesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Unit //Crash log in production use for future
        }
    }

}