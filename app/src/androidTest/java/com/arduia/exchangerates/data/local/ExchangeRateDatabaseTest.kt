package com.arduia.exchangerates.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arduia.exchangerates.data.CurrencyTypeDto
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class ExchangeRateDatabaseTest{
    private var db: ExchangeRateDatabase? = null
    private lateinit var currencyTypeDao: CurrencyTypeDao


    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                ExchangeRateDatabase::class.java
        ).build()
        currencyTypeDao = db!!.currencyTypeDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db?.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadCurrencyType() = runBlocking{

        val currencyType = CurrencyTypeDto(0,"USD", "United State Dollar")
        val users = listOf(currencyType)

        currencyTypeDao.insertAll(users)

        val insertedType = currencyTypeDao.getByCountryCode(currencyType.currencyCode)

        assertThat("USD", `is`(insertedType?.currencyCode))
        assertThat(currencyType.currencyName, `is`(insertedType?.currencyName))
        assertEquals(1, insertedType?.id)

        val secondCurrencyType = CurrencyTypeDto(0, "MMK", "Myanmar Kyat")
        currencyTypeDao.insertAll(listOf(secondCurrencyType))

        val secondInsertedType = currencyTypeDao.getByCountryCode(secondCurrencyType.currencyCode)

        assertEquals(secondCurrencyType.currencyCode, secondInsertedType?.currencyCode)
        assertEquals(2, secondInsertedType?.id)


    }
}
