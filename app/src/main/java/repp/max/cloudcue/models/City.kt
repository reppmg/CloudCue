package repp.max.cloudcue.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timeZone: Long? //GMT shift
) {
    fun localTime() : String? {
        timeZone ?: return null
        val format = SimpleDateFormat("hh:mm", Locale.UK)
        format.timeZone = TimeZone.getTimeZone("GMT%+d".format(timeZone))
        return format.format(Date())
    }
}
