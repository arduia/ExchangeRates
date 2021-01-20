package com.arduia.exchangerates.domain

/**
 * Created by Aung Ye Htet at 18/01/2021 5:45PM.
 */
interface Mapper <I,O>{

    fun map(input: I): O

}