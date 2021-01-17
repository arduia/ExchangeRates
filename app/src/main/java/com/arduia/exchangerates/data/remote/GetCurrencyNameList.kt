package com.arduia.exchangerates.data.remote

import com.arduia.exchangerates.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Created by Aung Ye Htet at 17/01/2021 9:29PM.
 */
class GetCurrencyNameList {

    data class Response(

        val success: Boolean,

        val currencyList: List<CurrencyNamePair>?,

        val errorInfo: ErrorInfo?
    )

    data class ErrorInfo(val code: Int, val info: String, val type: String)

    class CurrencyListDeserializer() : JsonDeserializer<Response> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Response {
            if (json == null) throw Exception("Cannot Deserialize")

            val jsonObject = json.asJsonObject
            if (!jsonObject.has("success") && !jsonObject.has("currencies"))
                throw Exception("There is no attributes")

            val successElement = jsonObject.get("success")
            val valueSuccess = successElement.asBoolean

            if (valueSuccess.not()) {
                //Success Fail
                val errorObject = jsonObject.get("error").asJsonObject

                val valueErrorCode = errorObject.get("code").asInt
                val valueErrorType = errorObject.get("type").asString
                val valueErrorInfo = errorObject.get("info").asString

                val error =
                    ErrorInfo(code = valueErrorCode, info = valueErrorInfo, type = valueErrorType)
                return Response(false, null, error)
            }

            val currenciesObject = jsonObject.get("currencies").asJsonObject
            val valueCurrenciesList = mutableListOf<CurrencyNamePair>()

            currenciesObject.keySet().forEach {
                val value = currenciesObject.get(it).asString
                val item = CurrencyNamePair(code = it, name = value)
                valueCurrenciesList.add(item)
            }

            return Response(valueSuccess, valueCurrenciesList, null)
        }
    }
}

data class CurrencyNamePair(val code: String, val name: String)
