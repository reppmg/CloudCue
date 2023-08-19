package repp.max.cloudcue.screens.weather_list.models

sealed class WeatherListState {
    object Loading : WeatherListState()
    data class WeatherList(
        val weathers: List<String>
    ) : WeatherListState()
}