package repp.max.cloudcue.screens.weather_list.permission.models

sealed class PermissionAction {

    object RequestPermission : PermissionAction()
    object OpenSettingsPermission : PermissionAction()
}