package com.arduia.exchangerates.data.ext

import com.arduia.exchangerates.data.exception.RepositoryException
import com.arduia.exchangerates.domain.ErrorResult
import com.arduia.exchangerates.domain.Result
import com.arduia.exchangerates.domain.SuccessResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Created by Aung Ye Htet at 18/01/2021 8:00 PM.
 */
//Map to Result Flow Chain
fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> {
    return this.map {
        SuccessResult(it) as Result<T>
    }.catch {
        emit(ErrorResult(RepositoryException(it)))
    }
}

//Map to Result type from raw value
inline fun <T> resultNullable(io: () -> T?): Result<T?> {
    return try {
        SuccessResult(io.invoke())
    } catch (e: Throwable) {
        ErrorResult(RepositoryException(e))
    }
}
inline fun <T> result(io: () -> T): Result<T> {
    return try {
        SuccessResult(io.invoke())
    } catch (e: Throwable) {
        ErrorResult(RepositoryException(e))
    }
}

fun <T: Throwable> repoErrorResult(e: T): ErrorResult{
    return ErrorResult(RepositoryException(e))
}