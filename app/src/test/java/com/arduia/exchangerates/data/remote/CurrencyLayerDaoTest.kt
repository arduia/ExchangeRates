package com.arduia.exchangerates.data.remote

import com.arduia.exchangerates.BuildConfig
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyLayerDaoTest {
    @Test
    fun testAPICalls() = runBlocking {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                GetCurrencyLive.Response::class.java,
                GetCurrencyLive.GetCurrencyLiveDeserializer()
            )
            .registerTypeAdapter(
                GetCurrencyNameList.Response::class.java,
                GetCurrencyNameList.CurrencyListDeserializer()
            )
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val currencyLayerDao = retrofit.create(CurrencyLayerDao::class.java)
        val currencyList = currencyLayerDao.getCurrencyLive(accessKey = "","USD")
        val result = currencyList.execute().body()!!
        println(result)

    }
}
