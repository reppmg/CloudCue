package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import repp.max.cloudcue.screens.city_details.model.CityDetailsState
import repp.max.cloudcue.screens.city_details.model.CityDetailsViewEvent

@Composable
fun CityDetailsScreen(
    viewModel: CityDetailsViewModel
) {
    val viewState by viewModel.state.collectAsState()
    when (val state = viewState) {
        is CityDetailsState.CityWeatherState -> {
            Column {
                Text(text = state.city)
                Text(text = state.weather.toString())
            }
        }
        CityDetailsState.InitState -> {}
    }
}