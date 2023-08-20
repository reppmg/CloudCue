package repp.max.cloudcue.api.models

data class CityTimeDto(
    val gmtOffset: Long, //in seconds
) {
    val gmtOffsetHours: Long get() = gmtOffset / 3600
}
