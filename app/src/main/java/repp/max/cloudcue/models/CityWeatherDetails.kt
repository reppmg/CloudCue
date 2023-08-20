package repp.max.cloudcue.models


typealias DailyForecast = HourlyForecast

data class CityWeatherDetails(
    val cityWeather: CityWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast> //3 days forward at 12pm
)

data class HourlyForecast(
    val timestamp: Long,
    val temperature: Temperature,
    val condition: Condition
)

data class Temperature(
    val current: Int,
    val min: Int,
    val max: Int
)

data class Condition(
    val name: String,
    val imageUrl: String
)