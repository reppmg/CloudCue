package repp.max.cloudcue.screens.weather_list.models

sealed class WeatherListAction {
    data class NavigateDetails(val city: String) : WeatherListAction()
}