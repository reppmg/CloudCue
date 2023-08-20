package repp.max.cloudcue.models

import repp.max.cloudcue.api.models.HourlyForecast

data class CityWeatherDetails (
    val cityWeather: CityWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<HourlyForecast> //3 days forward at 12pm
)