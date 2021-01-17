package com.arduia.exchangerates.data.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Created by Aung Ye Htet at 17/01/2021 9:25PM.
 */
class GetCurrencyLive {
    data class Response(

        val success: Boolean,

        val source: String?,

        val exchangeRate: List<CurrencyRatePair>?,

        val error: ErrorInfo?
    )

    data class ErrorInfo(val code: Int, val info: String)

    class GetCurrencyLiveDeserializer() : JsonDeserializer<Response> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Response {
            if (json == null) throw Exception("Cannot Deserialize")

            val jsonObject = json.asJsonObject

            if (!jsonObject.has("success") &&
                !jsonObject.has("source")
                && !jsonObject.has("quotes")
            )
                throw Exception("There is no attributes")

            val successElement = jsonObject.get("success")
            val valueSuccess = successElement.asBoolean

            if (valueSuccess.not()) {
                val errorObject = jsonObject.get("error").asJsonObject

                val valueErrorCode = errorObject.get("code").asInt
                val valueInfo = errorObject.get("info").asString
                val error = ErrorInfo(valueErrorCode, valueInfo)

                return Response(valueSuccess, source = null, exchangeRate = null, error = error)
            }

            val sourceElement = jsonObject.get("source")
            val valueSource = sourceElement.asString

            val valueExchangeRate = mutableListOf<CurrencyRatePair>()
            val exchangeRateObject = jsonObject.get("quotes").asJsonObject
            exchangeRateObject.keySet().forEach {
                //RateValue 3.67302
                val value = exchangeRateObject.get(it).asString
                //Remove Source Prefix. eg USDAED => source = USD, code= AED
                val code = it.substringAfter(valueSource)
                val item = CurrencyRatePair(code = code, rate = value)
                valueExchangeRate.add(item)
            }

            return Response(
                source = valueSource,
                success = valueSuccess,
                exchangeRate = valueExchangeRate,
                error = null
            )
        }
    }
}

data class CurrencyRatePair(val code: String, val rate: String)
