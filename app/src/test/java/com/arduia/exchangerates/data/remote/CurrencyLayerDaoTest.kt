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
                GetCurrencyLiveDto.Response::class.java,
                GetCurrencyLiveDto.GetCurrencyLiveDeserializer()
            )
            .registerTypeAdapter(
                GetCurrencyNameListDto.Response::class.java,
                GetCurrencyNameListDto.CurrencyListDeserializer()
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
