@file:OptIn(ExperimentalPermissionsApi::class)

package repp.max.cloudcue.screens.weather_list

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.navigation.Screens
import repp.max.cloudcue.screens.weather_list.models.WeatherListAction
import repp.max.cloudcue.screens.weather_list.models.WeatherListState
import repp.max.cloudcue.screens.weather_list.models.WeatherListViewEvent
import repp.max.cloudcue.screens.weather_list.permission.PermissionViewModel
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionAction
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionEvent
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionState
import timber.log.Timber

@Composable
fun CityWeatherListScreen(
    navController: NavHostController,
    viewModel: CityWeatherListViewModel,
    permissionViewModel: PermissionViewModel,
) {
    val context = LocalContext.current
    val androidPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            Timber.d("CityWeatherListScreen: $it ")
            permissionViewModel.apply(
                PermissionEvent.UpdatePermissionState(
                    if (it) PermissionStatus.Granted else PermissionStatus.Denied(
                        true
                    )
                )
            )
        }
    )
    permissionViewModel.apply(PermissionEvent.UpdatePermissionState(androidPermissionState.status))
    LaunchedEffect(key1 = viewModel) {
        launch {
            viewModel.viewActions.collect { action ->
                when (action) {
                    is WeatherListAction.NavigateDetails -> navController.navigate(
                        Screens.CityWeatherDetails(
                            action.city
                        ).route
                    )
                }
            }
        }
        launch {
            permissionViewModel.viewActions.collect { action ->
                when (action) {
                    PermissionAction.RequestPermission -> permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
//                        androidPermissionState.launchPermissionRequest()
                    PermissionAction.OpenSettingsPermission -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            val uri = Uri.fromParts("package", context.packageName, null)
                            data = uri
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
    Column {
        val viewModelPermissionState by permissionViewModel.state.collectAsState()
        when (val state = viewModelPermissionState) {
            is PermissionState.Denied -> PermissionRationale(permissionViewModel, state)
            else -> {}
        }
        val state by viewModel.state.collectAsState()
        with(state) {
            when (this) {
                is WeatherListState.Loading -> Progress()
                is WeatherListState.WeatherList -> WeatherList({
                    viewModel.apply(
                        WeatherListViewEvent.OnItemClicked(it)
                    )
                }, weathers)
            }
        }
    }
}


@Composable
fun WeatherList(
    onItemClicked: (CityWeather) -> Unit, list: List<CityWeather>
) {
    LazyColumn {
        items(list) {
            Box(Modifier.clickable { onItemClicked(it) }) {
                WeatherCard(
                    weather = it,
                )
            }
        }
    }
}


@Composable
fun Progress() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}