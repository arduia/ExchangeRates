package com.arduia.exchangerates.data.exception

import java.lang.Exception

class CacheException( override val cause: Throwable? = null, override val message: String? = null) : Exception()