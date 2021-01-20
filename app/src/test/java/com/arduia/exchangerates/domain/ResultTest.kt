package com.arduia.exchangerates.domain

import com.arduia.exchangerates.R
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.lang.Exception

class ResultTest{

    @Test
    fun shouldInitWork(){
        val successResult: Result<String> = Result.Success("Success")
        val errorResult: Result<String> = Result.Error(Exception("Error"))

        //Test Success Result initialization: Type Casting
        assertTrue(successResult is Result.Success)
        assertTrue(successResult !is ErrorResult)
        assertTrue(successResult !is Result.Loading)
        assertEquals("Success", successResult.data)
        assertNull((successResult as? Result.Error)?.exception)

        //Test Error Result
        assertTrue(errorResult is Result.Error)
        assertTrue(errorResult !is Result.Success)
        assertTrue(errorResult !is Result.Loading)
        assertNotNull((errorResult as? Result.Error)?.exception) //Exception must exist
        assertTrue((errorResult as? Result.Error)?.exception?.message == "Error")
        assertNull((errorResult as? Result.Success)?.data)
    }

    @Test
    fun shouldGetDataWork(){
        val result: Result<String> = Result.Success("Success")
        assertEquals("Success", result.getDataOrThrow())
    }

    @Test(expected = IOException::class)
    fun shouldGetErrorThrowWork(){
        val errorResult: Result<String> = Result.Error(IOException("network error"))
        assertEquals("network error", (errorResult as Result.Error).exception.message)
       val nothing =  errorResult.getDataOrThrow() // Error Must Throw
    }
}