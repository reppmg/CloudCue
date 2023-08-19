package repp.max.cloudcue.screens.model

sealed class Event {
    data class NavigateDetails(val city: String) : Event()
}