package repp.max.cloudcue.repository.util

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

object DateUtils {
    fun nextDay12pmDate(gmtOffset: Long): Date {
        val timeZone = TimeZone.getTimeZone("GMT%+d".format(gmtOffset))
        val calendar = Calendar.getInstance(timeZone)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }
    fun nextDayMidnightDate(gmtOffset: Long): Date {
        val timeZone = TimeZone.getTimeZone("GMT%+d".format(gmtOffset))
        val calendar = Calendar.getInstance(timeZone)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }
}