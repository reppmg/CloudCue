package repp.max.cloudcue.screens.weather_list.permission.models

sealed class PermissionState {
    object Undefined: PermissionState()
    data class Denied(
        val shouldNavigateToSettings: Boolean = false
    ): PermissionState()
    object DeniedDisplaySettings: PermissionState()
    object Granted: PermissionState()
}