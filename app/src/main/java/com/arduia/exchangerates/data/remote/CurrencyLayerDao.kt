package com.arduia.exchangerates.data.remote

import com.arduia.exchangerates.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Aung Ye Htet at 17/01/2021 9:15PM.
 */
interface CurrencyLayerDao {

    @GET("/live")
    fun getCurrencyLive(@Query("access_key") accessKey: String = BuildConfig.API_KEY, @Query("source") source: String = "USD"):
            Call<GetCurrencyLiveDto.Response>

    @GET("/list")
    fun getCurrencyNameList(@Query("access_key") accessKey: String = BuildConfig.API_KEY):
            Call<GetCurrencyNameListDto.Response>


}