package repp.max.cloudcue.models

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class City(
    val name: String,
    val location: Location,
    val gmtOffset: Long? //GMT shift
) {
    fun localTime() : String? {
        gmtOffset ?: return null
        val format = SimpleDateFormat("HH:mm", Locale.UK)
        format.timeZone = TimeZone.getTimeZone("GMT%+d".format(gmtOffset))
        Timber.d("localTime: time zone: ${format.timeZone}; ${"GMT%+d".format(gmtOffset)}")
        return format.format(Date())
    }
}
