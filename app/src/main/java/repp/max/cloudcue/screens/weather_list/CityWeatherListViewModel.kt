package repp.max.cloudcue.screens.weather_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repp.max.cloudcue.BaseViewModel
import repp.max.cloudcue.screens.weather_list.models.WeatherListAction
import repp.max.cloudcue.screens.weather_list.models.WeatherListState
import repp.max.cloudcue.screens.weather_list.models.WeatherListViewEvent
import repp.max.cloudcue.domain.GetWeatherListUseCase
import timber.log.Timber
import javax.inject.Inject

const val KEY_PERMISSION_ATTEMPTS = "permission_attempts"

@HiltViewModel
class CityWeatherListViewModel @Inject constructor(
    private val getWeatherListUseCase: GetWeatherListUseCase
) : BaseViewModel<WeatherListState, WeatherListAction, WeatherListViewEvent>(WeatherListState.Loading) {

    init {
        viewModelScope.launch {
            getWeatherListUseCase().collect {
                Timber.d("weather: $it")
                updateState(WeatherListState.WeatherList(it.filterNotNull()))
            }
        }
    }

    override fun processViewEvent(event: WeatherListViewEvent) {
        when (event) {
            is WeatherListViewEvent.OnItemClicked ->
                sendAction(WeatherListAction.NavigateDetails(event.cityWeather.city.name))
        }
    }
}

