package com.arduia.exchangerates.data.exception

import java.lang.Exception

/**
 * Created by Aung Ye Htet at 8:00 PM.
 */
//Every Exception from Repository Layer must wrap in this exception
class RepositoryException(e: Throwable): Exception(e)
