package repp.max.cloudcue.screens.model

sealed class State {
    object Loading : State()
    data class WeatherList(
        val weathers: List<String>
    ) : State()
}