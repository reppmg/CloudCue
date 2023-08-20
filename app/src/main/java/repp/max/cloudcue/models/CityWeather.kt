package repp.max.cloudcue.models

data class CityWeather(
    val city: City,
    val currentTemp: Double,
    val conditionName: String?,
    val conditionImageUrl: String?,
)
