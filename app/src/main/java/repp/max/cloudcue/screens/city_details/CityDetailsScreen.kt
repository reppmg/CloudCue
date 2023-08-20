package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import repp.max.cloudcue.api.models.Condition
import repp.max.cloudcue.api.models.HourlyForecast
import repp.max.cloudcue.api.models.Main
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.screens.city_details.model.DetailsState
import repp.max.cloudcue.screens.weather_list.WeatherCardContent
import repp.max.cloudcue.ui.theme.Dimen
import java.util.Date

@Composable
fun CityDetailsScreen(
    viewModel: CityDetailsViewModel
) {
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
            .padding(Dimen.base)
    ) {
        WeatherCardContent(weather = weather.cityWeather)
    }
}

@Preview
@Composable
fun Preview() {
    Content(
        weather = CityWeatherDetails(
            CityWeather(
                City("London", 0.0, 0.0, 1),
                23.0,
                "scattered clouds",
                "http://openweathermap.org/img/wn/50d@2x.png"
            ),
            listOf(
                HourlyForecast(
                    Date().time,
                    Main(
                        temp = 29.0
                    ),
                    listOf(
                        Condition(
                            id = 300,
                            main = "Rain",
                            description = "heavy reain",
                            icon = "http://openweathermap.org/img/wn/50d@2x.png"
                        )
                    ),
                )
            ),
            listOf(
                HourlyForecast(
                    Date().time,
                    Main(
                        temp = 29.0
                    ),
                    listOf(
                        Condition(
                            id = 300,
                            main = "Rain",
                            description = "heavy reain",
                            icon = "http://openweathermap.org/img/wn/50d@2x.png"
                        )
                    ),
                )
            )
        )
    )
}