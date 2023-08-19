package repp.max.cloudcue.screens.weather_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.onEach
import repp.max.cloudcue.navigation.Screens
import repp.max.cloudcue.screens.weather_list.models.WeatherListAction
import repp.max.cloudcue.screens.weather_list.models.WeatherListState
import repp.max.cloudcue.screens.weather_list.models.WeatherListViewEvent
import timber.log.Timber


@Composable
fun CityWeatherListScreen(
    navController: NavHostController, viewModel: CityWeatherListViewModel
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.viewActions.collect {action ->
            when (action) {
                is WeatherListAction.NavigateDetails -> {
                    navController.navigate(
                        Screens.CityWeatherDetails(action.city).route
                    )
                }
            }
        }
    }
    val state by viewModel.state.collectAsState()
    with(state) {
        when (this) {
            is WeatherListState.Loading -> Progress()
            is WeatherListState.WeatherList -> WeatherList(navController, viewModel, weathers)
        }
    }
}

@Composable
fun WeatherList(
    navController: NavHostController, viewModel: CityWeatherListViewModel, list: List<String>
) {
    LazyColumn {
        items(list) { weather ->
            Text(text = "weather for $weather", modifier = Modifier.clickable {
                viewModel.apply(
                    WeatherListViewEvent.OnItemClicked(
                        weather
                    )
                )
            })
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