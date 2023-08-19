package repp.max.cloudcue.screens.weather_list.models

sealed class WeatherListViewEvent {
    data class OnItemClicked(val city: String) : WeatherListViewEvent()
}
