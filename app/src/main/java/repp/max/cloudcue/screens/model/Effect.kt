package repp.max.cloudcue.screens.model

sealed class Effect {
    data class OnItemClicked(val  city: String) : Effect()
}
