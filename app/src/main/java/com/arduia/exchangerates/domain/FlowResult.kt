package com.arduia.exchangerates.domain


import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * Created by Aung Ye Htet at 17/01/20201 at 10:36 AM.
 */

//Extension Class and Methods for Result in Flow Pipeline
typealias FlowResult<T> = Flow<Result<T>>

fun <T> FlowResult<T>.awaitValueOrError(): T = runBlocking<T> {

    var firstSuccess: T? = null

    try {
        collect {
            if (it is Result.Success) {
                firstSuccess = it.data
                throw AbortFlowException()
            }
            if (it is Result.Error) throw Exception(it.exception)
        }
    } catch (e: AbortFlowException) {
        //Must occur catch to absorb collect listener
    }

    return@runBlocking firstSuccess ?: throw Exception("Await Value: Empty Result")
}

class AbortFlowException : CancellationException()

fun <T> FlowResult<T>.onSuccess(success: suspend (data: T) -> Unit): FlowResult<T> {
    return onEach {
        if (it is SuccessResult) success.invoke(it.data)
    }
}

fun <T> FlowResult<T>.onLoading(loading: (Boolean) -> Unit): FlowResult<T> {
    return onEach {
        if (it is LoadingResult) loading.invoke(true) else loading.invoke(false)
    }
}

fun <T> FlowResult<T>.onError(error: (Exception) -> Unit): FlowResult<T> {
    return onEach {
        if (it is ErrorResult) error.invoke(it.exception)
    }
}