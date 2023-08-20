package repp.max.cloudcue.screens.weather_list.models

import repp.max.cloudcue.models.CityWeather

sealed class WeatherListState {
    object Loading : WeatherListState()
    data class WeatherList(
        val weathers: List<CityWeather>
    ) : WeatherListState()
}