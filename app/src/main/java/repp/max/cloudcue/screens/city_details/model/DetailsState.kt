package repp.max.cloudcue.screens.city_details.model

import repp.max.cloudcue.models.CityWeatherDetails

sealed class DetailsState {
    object InitState : DetailsState()
    data class DetailsLoadedState(
        val city: CityWeatherDetails
    ) : DetailsState()
    object ErrorState: DetailsState()
}