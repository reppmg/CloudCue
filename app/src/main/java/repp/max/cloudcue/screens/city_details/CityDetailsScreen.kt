package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.screens.city_details.model.CityDetailsAction
import repp.max.cloudcue.screens.city_details.model.DetailsState
import repp.max.cloudcue.screens.weather_list.WeatherCardContent
import repp.max.cloudcue.ui.theme.Dimen

@Composable
fun CityDetailsScreen(
    navController: NavController,
    viewModel: CityDetailsViewModel
) {
    LaunchedEffect(key1 = viewModel) {
        launch {
            viewModel.viewActions.collect { action ->
                when (action) {
                    CityDetailsAction.Exit -> navController.popBackStack()
                }
            }
        }
    }
    val viewState by viewModel.state.collectAsState()
    when (val state = viewState) {
        is DetailsState.DetailsLoadedState -> {
            Content(state.city)
        }

        DetailsState.InitState -> {}
        DetailsState.ErrorState -> {}
    }
}

@Composable
fun Content(weather: CityWeatherDetails) {
    Card(
        Modifier
            .padding(all = Dimen.baseHorizontal)
    ) {
        Column {
            WeatherCardContent(weather = weather.cityWeather)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 9.dp),
                text = "Today"
            )
            TodayHourly(weather)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 9.dp),
                text = "Forecast"
            )
            Forecast(weather)
        }
    }
}

@Composable
private fun TodayHourly(weather: CityWeatherDetails) {
    Box(
        Modifier.padding(vertical = Dimen.baseVertical)
    ) {
        LazyRow {
            items(weather.hourly) { hourWeather ->
                TodayForecastItem(hourWeather)
            }
        }
    }
}


