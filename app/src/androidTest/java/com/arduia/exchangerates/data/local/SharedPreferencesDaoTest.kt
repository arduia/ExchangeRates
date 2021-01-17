package com.arduia.exchangerates.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Aung Ye Htet at 17/01/2021 7:00 PM.
 */
@RunWith(AndroidJUnit4::class)
class SharedPreferencesDaoTest{

    private lateinit var preferenceDao: PreferencesDao

    @Before
    fun createSharedPreferenceDao(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferenceDao = SharedPreferencesDao(context)
    }

    @Test
    fun testChooseCurrencyType(): Unit = runBlocking{

        //Success Test
        val syncDate = Date().time
        val isSuccess = preferenceDao.setLastSyncDate(syncDate)
        assertTrue(isSuccess)

        //InsertedValue
        val setSyncDateValue = preferenceDao.getLastSyncDateSync()
        assertEquals(syncDate, setSyncDateValue)

        //SecondValueSetGet Test
        val secondDate = Date().time
        val isSuccessForSecond = preferenceDao.setLastSyncDate(secondDate)
        assertTrue(isSuccessForSecond)

        val valueSyncDateSecond = preferenceDao.getLastSyncDateSync()
        assertEquals(secondDate, valueSyncDateSecond)
    }

    @Test
    fun testSelectedCurrencyType(): Unit = runBlocking{

        //Get Set Synchronously
        val selectedType = "USD"
        val isSuccess = preferenceDao.setSelectedCurrencyType(selectedType)
        assertTrue(isSuccess)

        val valueSet = preferenceDao.getSelectedCurrencyTypeSync()
        assertEquals(selectedType, valueSet)

        //Second Get Set
        val secondSelectedType = "MMK"
        val isSuccessForSecond = preferenceDao.setSelectedCurrencyType(secondSelectedType)
        assertTrue(isSuccessForSecond)

        val valueSetForSecond = preferenceDao.getSelectedCurrencyTypeSync()
        assertEquals(secondSelectedType, valueSetForSecond)

        //Flow Value Changes
        val setValueFlow = preferenceDao.getSelectedCurrencyTypeFlow()
        assertEquals(secondSelectedType, setValueFlow.first())

        preferenceDao.setSelectedCurrencyType(selectedType)
        assertEquals(selectedType, setValueFlow.first())

    }
}