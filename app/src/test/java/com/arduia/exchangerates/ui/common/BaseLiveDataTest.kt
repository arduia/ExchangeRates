package com.arduia.exchangerates.ui.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

/**
 * Created by Aung Ye Htet on 20/01/2021.
 */
//Also available in MVVM-Core: https://github.com/arduia/mvvm-core
internal class BaseLiveDataTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldInitWithValueReturn() {
        val initValue = "init"
        val baseLiveData = BaseLiveData(initValue = initValue)
        baseLiveData.observeForTesting {
            val value = baseLiveData.getOrAwaitValue()
            assert(value == initValue)
        }
    }

    @Test
    fun shouldSetReturnValue(){
        val setValue = "set"
        val liveData = BaseLiveData<String>()
        assertNull(liveData.value)
        liveData.value = setValue
        val value = liveData.getOrAwaitValue {  }
        assert(value == setValue)
        liveData.value = null
        val value2 = liveData.getOrAwaitValue {  }
        assert(value2 == null)
    }

    @Test
    fun shouldPostReturnValue(){
        val postValue = "post"
        val liveData = BaseLiveData<String>()
        liveData.postValue(postValue)
        val value = liveData.getOrAwaitValue {  }
        assert(value == postValue)
        liveData.postValue(null)
        val value2 = liveData.getOrAwaitValue {  }
        assert(value2 == null)
    }

    @Test
    fun shouldAssignExtensionsReturnValue(){
        val postValue = "postValue"
        val liveData = BaseLiveData<String>()

        liveData post  postValue
        val postReturnValue = liveData.getOrAwaitValue()
        assert(postReturnValue == postValue)

        val setValue = "setValue"
        liveData set  setValue
        val setReturnValue = liveData.getOrAwaitValue {  }
        assert(setReturnValue == setValue)
    }
}