package repp.max.cloudcue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE, ACTION, EVENT>(initialState: STATE) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val singleThreadContext = Dispatchers.Default.limitedParallelism(1)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _viewActions = Channel<ACTION>(Channel.BUFFERED)
    val viewActions = _viewActions.receiveAsFlow()

    protected val viewEvents = Channel<EVENT>()

    init {
        viewModelScope.launch {
            viewEvents.consumeEach(::processViewEvent)
        }
    }

    fun apply(event: EVENT) {
        viewModelScope.launch {
            viewEvents.send(event)
        }
    }

    protected fun reduce(reduce: STATE.() -> STATE) {
        updateState(_state.value.reduce())
    }

    protected fun updateState(newState: STATE) {
        viewModelScope.launch(singleThreadContext) {
            _state.emit(newState)
        }
    }

    protected fun sendAction(action: ACTION) {
        viewModelScope.launch {
            _viewActions.send(action)
        }
    }

    protected abstract fun processViewEvent(event: EVENT)

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared: ")
    }
}