package repp.max.cloudcue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import repp.max.cloudcue.screens.model.Effect
import repp.max.cloudcue.screens.model.State
import repp.max.cloudcue.screens.model.ViewEvent

open class BaseViewModel : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val singleThreadContext = Dispatchers.Default.limitedParallelism(1)

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _viewEffects = Channel<Effect>(Channel.BUFFERED)
    val viewEffects = _viewEffects.receiveAsFlow()

    protected val viewEvents = MutableSharedFlow<ViewEvent>()

    init {
        viewModelScope.launch {
            viewEvents.onEach(::processIntent)
        }
    }

    fun apply(event: ViewEvent) {
        viewModelScope.launch {
            viewEvents.emit(event)
        }
    }

    private fun reduce(reduce: State.() -> State) {
        updateState(_state.value.reduce())
    }

    protected fun updateState(newState: State) {
        viewModelScope.launch(singleThreadContext) {
            _state.emit(newState)
        }
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _viewEffects.send(effect)
        }
    }

    protected open fun processIntent(event: ViewEvent) {

    }
}