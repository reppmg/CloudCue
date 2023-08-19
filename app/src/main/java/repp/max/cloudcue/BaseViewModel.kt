package repp.max.cloudcue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE, EFFECT, EVENT>(initialState: STATE) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val singleThreadContext = Dispatchers.Default.limitedParallelism(1)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _viewEffects = Channel<EFFECT>(Channel.BUFFERED)
    val viewActions = _viewEffects.receiveAsFlow()

    protected val viewEvents = Channel<EVENT>()

    init {
        viewModelScope.launch {
            viewEvents.consumeEach(::processViewEvent)
        }
    }

    fun apply(event: EVENT) {
        viewModelScope.launch {
            Timber.d("apply: ")
            viewEvents.send(event)
            Timber.d("apply: ")
        }
    }

    private fun reduce(reduce: STATE.() -> STATE) {
        updateState(_state.value.reduce())
    }

    protected fun updateState(newState: STATE) {
        viewModelScope.launch(singleThreadContext) {
            _state.emit(newState)
        }
    }

    protected fun sendAction(effect: EFFECT) {
        viewModelScope.launch {
            _viewEffects.send(effect)
        }
    }

    protected abstract fun processViewEvent(event: EVENT)

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared: ")
    }
}