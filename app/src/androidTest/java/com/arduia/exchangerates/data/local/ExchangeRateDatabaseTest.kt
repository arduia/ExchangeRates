package com.arduia.exchangerates.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arduia.exchangerates.data.CacheExchangeRateDto
import com.arduia.exchangerates.data.CurrencyTypeDto
import com.arduia.exchangerates.domain.Amount
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

/**
 * Created by Aung Ye Htet at 17/01/2021 5:00PM
 */
@RunWith(AndroidJUnit4::class)
class ExchangeRateDatabaseTest{
    private var db: ExchangeRateDatabase? = null
    private lateinit var currencyTypeDao: CurrencyTypeDao
    private lateinit var cacheExchangeRateDao: CacheExchangeRateDao
    private lateinit var exchangeRatesDao: ExchangeRatesDao


    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                ExchangeRateDatabase::class.java
        ).build()
        currencyTypeDao = db!!.currencyTypeDao
        cacheExchangeRateDao = db!!.cacheExchangeRateDao
        exchangeRatesDao = db!!.exchangeRateDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db?.close()
    }

    @Test
    @Throws(Exception::class)
    fun testChooseCurrencyTypeDao() = runBlocking{

        //InsertAl
        val currencyType = CurrencyTypeDto(0,"USD", "United State Dollar")
        currencyTypeDao.insertAll( listOf(currencyType))

        //GetByCurrencyCode
        val insertedType = currencyTypeDao.getByCurrencyCode(currencyType.currencyCode)

        assertThat("USD", `is`(insertedType?.currencyCode))
        assertThat(currencyType.currencyName, `is`(insertedType?.currencyName))
        assertEquals(1, insertedType?.id)

        val secondCurrencyType = CurrencyTypeDto(0, "MMK", "Myanmar Kyat")
        currencyTypeDao.insertAll(listOf(secondCurrencyType))
        val secondInsertedType = currencyTypeDao.getByCurrencyCode(secondCurrencyType.currencyCode)
        assertEquals(secondCurrencyType.currencyCode, secondInsertedType?.currencyCode)
        assertEquals(2, secondInsertedType?.id)

        //GetByCurrencyCodeFlow
        val currencyTypeFlow = currencyTypeDao.getByCurrencyCodeFlow(currencyType.currencyCode)
        assertEquals(currencyType.currencyCode, currencyTypeFlow.first().currencyCode)
        assertEquals(currencyType.currencyName, currencyTypeFlow.first().currencyName)

        //AllDataSourceFactory
        val factory = currencyTypeDao.getAllDataSource()
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0,10)
        assertEquals(2, list.size)
        assertEquals(currencyType.currencyCode, list[0].currencyCode)
        assertEquals(secondCurrencyType.currencyCode, list[1].currencyCode)

        //OnConflictTest
        currencyTypeDao.insertAll(listOf(secondCurrencyType))
        val listAfterSameItems = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertNotEquals(3, listAfterSameItems.size)


        //DeleteAll
        currencyTypeDao.deleteAll()
        val insertedValue = currencyTypeDao.getByCurrencyCode("USD")
        assertNull(insertedValue)
    }

    @Test
    fun testCacheExchangeRateDao() = runBlocking{

        //InsertAll
        val item = CacheExchangeRateDto(0, "USD", "2.56")
        cacheExchangeRateDao.insertAll(listOf(item))

        val insertedItems = cacheExchangeRateDao.getAll()
        assertEquals(1, insertedItems.size)
        assertEquals(item.currencyCode, insertedItems.first().currencyCode)
        assertEquals(item.exchangeRate, insertedItems.first().exchangeRate)

        val secondItem = CacheExchangeRateDto(0, "MMK", "3.45")
        cacheExchangeRateDao.insertAll(listOf(secondItem))
        val secondInsertedItems = cacheExchangeRateDao.getAll()
        assertEquals(2, secondInsertedItems.size)
        assertEquals(secondItem.currencyCode, secondInsertedItems[1].currencyCode)

        //onConflict Test
        cacheExchangeRateDao.insertAll(listOf(secondItem))
        val sameInsertedItems = cacheExchangeRateDao.getAll()
        assertEquals(2, sameInsertedItems.size)
        assertEquals(secondItem.currencyCode, sameInsertedItems[1].currencyCode)

    }

    @Test
    fun testExchangeRateDao() = runBlocking{

        cacheExchangeRateDao.deleteAll()
        currencyTypeDao.deleteAll()

        val currencyItemOne = CurrencyTypeDto(0, "USD", "United State Dollar")
        val currencyTypeTwo = CurrencyTypeDto(0, "MMK", "Myanmar Kyat")

        currencyTypeDao.insertAll(listOf(currencyItemOne,currencyTypeTwo))

        val rateOne = CacheExchangeRateDto(0, "USD", "3.65")
        val rateTwo = CacheExchangeRateDto(0, "MMK", "77.4")
        cacheExchangeRateDao.insertAll(listOf(rateOne, rateTwo))

        //Amount Test
        val factory = exchangeRatesDao.getAllDataSource()
        val exchangeRateList = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)

        assertEquals(2, exchangeRateList.size)
        assertEquals(currencyItemOne.currencyCode, exchangeRateList.first().currencyCode)
        assertEquals(currencyItemOne.currencyName, exchangeRateList.first().currencyName)
        assertEquals(rateOne.exchangeRate.toFloat(), exchangeRateList.first().exchangeRate.toString().toFloat())

        assertEquals(currencyTypeTwo.currencyCode, exchangeRateList[1].currencyCode)
        assertEquals(currencyTypeTwo.currencyName, exchangeRateList[1].currencyName)
        assertEquals(rateTwo.exchangeRate.toFloat(), exchangeRateList[1].exchangeRate.toString().toFloat())

        val currencyThree = CurrencyTypeDto(0,"EUR", "Euro")
        currencyTypeDao.insertAll(listOf(currencyThree))
        val list = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertNotEquals(3, list.size)

        val rateThree = CacheExchangeRateDto(0, "EUR", "59.10")
        cacheExchangeRateDao.insertAll(listOf(rateThree))
        val listThree = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)
        assertEquals(3, listThree.size)
    }

}
