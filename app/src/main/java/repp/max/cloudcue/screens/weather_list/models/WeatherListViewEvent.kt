package repp.max.cloudcue.screens.weather_list.models

import repp.max.cloudcue.models.CityWeather

sealed class WeatherListViewEvent {
    data class OnItemClicked(val cityWeather: CityWeather) : WeatherListViewEvent()
}
