package repp.max.cloudcue.models

data class CityWeather(
    val city: City,
    val currentTemp: Double,
    val condition: Condition
)
