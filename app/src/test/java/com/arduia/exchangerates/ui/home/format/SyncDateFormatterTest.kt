package com.arduia.exchangerates.ui.home.format

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aung Ye Htet at 17/01/2021 7:39 PM.
 */
class SyncDateFormatterTest {

    @Test
    fun shouldDateRangeFormatWork() {

        val syncDateFormatter: DateFormatter = SyncDateFormatter()
        val date = Calendar.getInstance()
        val todayFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val expectedValue = "Today " + todayFormat.format(date.timeInMillis) //eg. Today 1:00 PM
        assertEquals(expectedValue, syncDateFormatter.format(date.timeInMillis))

        //Yesterday
        date[Calendar.DAY_OF_MONTH] -=1
        val exceptedYesterdayValue = "Yesterday "+todayFormat.format(date.timeInMillis) //eg. Yesterday 1:00 PM
        assertEquals(exceptedYesterdayValue, syncDateFormatter.format(date.timeInMillis))

        //Specific
        val exceptedSpecificValue = "2008 Jan 17"
        date.set(2008, 0, 17, 19, 41)
        assertEquals(exceptedSpecificValue, syncDateFormatter.format(date.timeInMillis))
    }
}