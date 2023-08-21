package repp.max.cloudcue.screens.weather_list.permission

import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repp.max.cloudcue.BaseViewModel
import repp.max.cloudcue.Constants
import repp.max.cloudcue.domain.RequestLocationUseCase
import repp.max.cloudcue.screens.weather_list.KEY_PERMISSION_ATTEMPTS
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionAction
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionEvent
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionState
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionState.*
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val storage: SharedPreferences,
    private val requestLocationUseCase: RequestLocationUseCase
) : BaseViewModel<PermissionState, PermissionAction, PermissionEvent>(Undefined) {

    override fun processViewEvent(event: PermissionEvent) {
        when (event) {
            PermissionEvent.OnPermissionRequested -> requestPermission()
            PermissionEvent.OnSettingsOpenClicked -> launchOpenPermissionSettings()

            is PermissionEvent.UpdatePermissionState -> {
                Timber.d("processViewEvent: ${event.status}")
                if (event.status == state.value) return

                when (event.status) {
                    is PermissionStatus.Denied -> if (attemptsMade < Constants.showPermissionAttempts) {
                        if (state.value !is Denied)
                            requestPermission()
                        updateState(
                            Denied()
                        )
                    } else {
                        updateState(
                            Denied(
                                shouldNavigateToSettings = true
                            )
                        )
                    }

                    PermissionStatus.Granted -> {
                        updateState(Granted)
                        viewModelScope.launch {
                            requestLocationUseCase()
                        }
                        storage.edit { remove(KEY_PERMISSION_ATTEMPTS) }
                    }

                    else -> {}
                }
            }
        }
    }

    private val attemptsMade: Int
        get() = storage.getInt(KEY_PERMISSION_ATTEMPTS, 0)

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            handleAndroid11Permission()
        } else {
            sendLaunchAction()
        }
    }

    private fun handleAndroid11Permission() {
        if (attemptsMade >= Constants.android11PermissionAttempts) {
            launchOpenPermissionSettings()
        } else {
            sendLaunchAction()
        }
    }

    private fun sendLaunchAction() {
        storage.edit {
            putInt(KEY_PERMISSION_ATTEMPTS, attemptsMade + 1)
        }
        Timber.d("sendLaunchAction: $attemptsMade")
        sendAction(PermissionAction.RequestPermission)
    }

    private fun launchOpenPermissionSettings() {
        Timber.d("launchOpenPermissionSettings: ")
        sendAction(PermissionAction.OpenSettingsPermission)
    }
}