package repp.max.cloudcue.screens.city_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import repp.max.cloudcue.BaseViewModel
import repp.max.cloudcue.domain.GetWeatherDetailsUseCase
import repp.max.cloudcue.screens.city_details.model.CityDetailsAction
import repp.max.cloudcue.screens.city_details.model.DetailsState
import repp.max.cloudcue.screens.city_details.model.DetailsState.*
import repp.max.cloudcue.screens.city_details.model.CityDetailsViewEvent


class CityDetailsViewModel @AssistedInject constructor(
    @Assisted cityName: String,
    private val getWeatherDetailsUseCase: GetWeatherDetailsUseCase
) :
    BaseViewModel<DetailsState, CityDetailsAction, CityDetailsViewEvent>(InitState) {

    @AssistedFactory
    interface AssistedViewModelFactory {
        fun create(city: String): CityDetailsViewModel
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
        getWeatherDetailsUseCase.invoke(cityName)
            .onEach { updateState(DetailsLoadedState(it)) }
            .catch { sendAction(CityDetailsAction.Exit) }
            .launchIn(viewModelScope)
    }

    override fun processViewEvent(event: CityDetailsViewEvent) {

    }
}