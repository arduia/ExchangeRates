package com.arduia.exchangerates.data.exception

import java.io.IOException
import java.lang.Exception

/**
 * Created by Aung Ye Htet on 18/01/2021.
 */
class NoConnectionException : IOException()

class NoInternetException : IOException()

class ServerErrorException(errorCode: Int, message: String) : Exception(message)