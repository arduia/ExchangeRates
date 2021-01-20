package com.arduia.exchangerates.di

import android.content.Context
import com.arduia.exchangerates.BuildConfig
import com.arduia.exchangerates.data.NoConnectionInterceptor
import com.arduia.exchangerates.data.local.CacheExchangeRateDao
import com.arduia.exchangerates.data.local.CurrencyTypeDao
import com.arduia.exchangerates.data.local.ExchangeRateDatabase
import com.arduia.exchangerates.data.local.ExchangeRatesDao
import com.arduia.exchangerates.data.remote.CurrencyLayerDao
import com.arduia.exchangerates.data.remote.GetCurrencyLiveDto
import com.arduia.exchangerates.data.remote.GetCurrencyNameListDto
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Aung Ye Htet on 18/01/2021
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyLayerDao(retrofit: Retrofit): CurrencyLayerDao{
        return retrofit.create(CurrencyLayerDao::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRateDao(db: ExchangeRateDatabase): ExchangeRatesDao = db.exchangeRateDao

    @Provides
    @Singleton
    fun provideCurrencyListDao(db: ExchangeRateDatabase): CurrencyTypeDao = db.currencyTypeDao

    @Provides
    @Singleton
    fun provideCacheExchangeRateDao(db: ExchangeRateDatabase): CacheExchangeRateDao = db.cacheExchangeRateDao

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {

        //HttpInterceptor for HttpLogs
         val collector = ChuckerCollector(
                context = context,
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

         val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(collector)
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .build()

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(NoConnectionInterceptor(context))
                .addInterceptor(chuckerInterceptor)
                .build()

        val gson = GsonBuilder()
                .registerTypeAdapter(
                        GetCurrencyLiveDto.Response::class.java,
                        GetCurrencyLiveDto.GetCurrencyLiveDeserializer()
                )
                .registerTypeAdapter(
                        GetCurrencyNameListDto.Response::class.java,
                        GetCurrencyNameListDto.CurrencyListDeserializer()
                )
                .create()

        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}