package repp.max.cloudcue.screens.model

sealed class Effect {
    data class NavigateDetails(val city: String) : Effect()
}