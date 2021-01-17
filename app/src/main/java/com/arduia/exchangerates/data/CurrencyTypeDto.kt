package com.arduia.exchangerates.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Aung Ye Htet at 17/01/2021 1:38PM.
 */
@Entity(tableName = CurrencyTypeDto.TABLE_NAME)
data class CurrencyTypeDto(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,

    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("currency_name")
    val currencyName: String
) {
    companion object {
        const val TABLE_NAME = "currency_types"
    }
}
