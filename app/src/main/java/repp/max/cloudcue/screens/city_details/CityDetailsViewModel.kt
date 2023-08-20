package repp.max.cloudcue.screens.city_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import repp.max.cloudcue.BaseViewModel
import repp.max.cloudcue.screens.city_details.model.CityDetailsAction
import repp.max.cloudcue.screens.city_details.model.CityDetailsState
import repp.max.cloudcue.screens.city_details.model.CityDetailsState.*
import repp.max.cloudcue.screens.city_details.model.CityDetailsViewEvent
import kotlin.random.Random


class CityDetailsViewModel @AssistedInject constructor(
    @Assisted city: String
) :
    BaseViewModel<CityDetailsState, CityDetailsAction, CityDetailsViewEvent>(InitState) {

    @AssistedFactory
    interface AssistedViewModelFactory {
        fun create(city: String) : CityDetailsViewModel
    }
    companion object {
        fun factory(
            factory: AssistedViewModelFactory,
            city: String
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return factory.create(city) as T
            }
        }
    }

    init {
        updateState(
            CityWeatherState(
                Random.nextInt(-60, 50),
                city
            )
        )
    }

    override fun processViewEvent(event: CityDetailsViewEvent) {

    }
}