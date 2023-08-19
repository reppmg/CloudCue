package repp.max.cloudcue.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import repp.max.cloudcue.screens.model.Event
import repp.max.cloudcue.screens.model.Effect
import repp.max.cloudcue.screens.model.State
import repp.max.cloudcue.service.GetWeatherListUseCase
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel @Inject constructor(
    private val getWeatherListUseCase: GetWeatherListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _viewEffects = Channel<Event>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    private val viewIntents = MutableSharedFlow<Effect>()

    fun apply(effect: Effect) {
        viewModelScope.launch {
            viewIntents.emit(effect)
        }
    }

    init {
        viewModelScope.launch {
            viewIntents.onEach(::processIntent)
        }
        viewModelScope.launch {
            getWeatherListUseCase.getList().collect {
                //todo threading
                _state.emit(State.WeatherList(it))

            }
        }
    }

    private fun processIntent(effect: Effect) {
        when (effect) {
            is Effect.OnItemClicked -> {
                viewModelScope.launch {
                    _viewEffects.send(Event.NavigateDetails(effect.city))
                }
            }
        }
    }
}