package repp.max.cloudcue.api.models

data class CityTimeDto (
    val gmtOffset: Long, //in seconds
) {
    val gmtOffsetHours : Long = gmtOffset / 3600
}
