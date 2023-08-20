package repp.max.cloudcue.screens.weather_list.permission.models

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus

sealed class PermissionEvent {

    @OptIn(ExperimentalPermissionsApi::class)
    data class UpdatePermissionState(val status: PermissionStatus): PermissionEvent()
    object OnPermissionRequested: PermissionEvent()
    object OnSettingsOpenClicked: PermissionEvent()
}