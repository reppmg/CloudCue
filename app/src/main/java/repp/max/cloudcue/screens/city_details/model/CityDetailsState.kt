package repp.max.cloudcue.screens.city_details.model

sealed class CityDetailsState {
    object InitState : CityDetailsState()
    data class CityWeatherState(
        val weather: Int,
        val city: String
    ) : CityDetailsState()
}