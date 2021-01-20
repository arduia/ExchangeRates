package com.arduia.exchangerates.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.lang.Exception

class FlowResultKtTest{

    @Test
    fun shouldInitWork(): Unit = runBlocking{
        val resultFlow = flow<Result<String>> {
            emit(Result.Success("success"))
            emit(Result.Error(IOException("error")))
        }

        val item = resultFlow.first()
        assertEquals("success", item.data)

        val resultList = resultFlow.toList()
        assertEquals(2, resultList.size)
        assertTrue(resultFlow.first() is Result.Success)
        assertNull(resultList[1].data )
        assertNotNull(resultList[1] as? Result.Error)
    }
}