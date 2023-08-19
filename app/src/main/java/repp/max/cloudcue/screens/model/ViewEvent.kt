package repp.max.cloudcue.screens.model

sealed class ViewEvent {
    data class OnItemClicked(val  city: String) : ViewEvent()
}
