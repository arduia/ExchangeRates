package com.arduia.exchangerates.ui.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by Aung Ye Htet on 20/01/2021.
 */
//Also available in MVVM-Core: https://github.com/arduia/mvvm-core
class EventObserverTest {

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    @Test
    fun shouldSingleEventOccur() {

        val lifecycleOwner = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)

        val liveData = MutableLiveData<Event<String>>()
        var eventCount = 0
        val eventValue1 = "event1"
        val event = Event(eventValue1)

        liveData.observe(lifecycleOwner, EventObserver { value ->
            assertThat(value, `is`(eventValue1))
            eventCount++
            assertEquals(1, eventCount)
        })

        liveData.value = event
        lifecycle.makeFakeConfigurationChanges()
        assertThat(liveData.value?.hashCode(), `is`(event.hashCode()))
        liveData.value?.let {
            assertTrue(it.hasHandled)
        }
        liveData.value = null
        lifecycle.makeFakeConfigurationChanges()
    }
}