package repp.max.cloudcue.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import repp.max.cloudcue.screens.model.State
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun CityWeatherListScreen(
    navController: NavHostController,
    viewModel: CityWeatherViewModel
) {
    val state by viewModel.state.collectAsState()
    with(state) {
        when (this) {
            is State.Loading -> Progress()
            is State.WeatherList -> WeatherList(navController, viewModel, weathers)
        }
    }
}

@Composable
fun WeatherList(
    navController: NavHostController,
    viewModel: CityWeatherViewModel,
    list: List<String>
) {
    LazyColumn {
        items(list) { weather ->
            Text(text = "weather for $weather")
        }
    }
}

@Composable
fun Progress() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}