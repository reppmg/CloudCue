package repp.max.cloudcue.screens

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repp.max.cloudcue.BaseViewModel
import repp.max.cloudcue.screens.model.Effect
import repp.max.cloudcue.screens.model.State
import repp.max.cloudcue.screens.model.ViewEvent
import repp.max.cloudcue.service.GetWeatherListUseCase
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    private val getWeatherListUseCase: GetWeatherListUseCase
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            getWeatherListUseCase.getList().collect {
                updateState(State.WeatherList(it))
            }
        }
    }

    override fun processIntent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnItemClicked -> {
                sendEffect(Effect.NavigateDetails(event.city))
            }
        }
    }
}