package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import repp.max.cloudcue.api.models.ConditionDto
import repp.max.cloudcue.api.models.HourlyForecastDto
import repp.max.cloudcue.api.models.MainDto
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.models.HourlyForecast
import repp.max.cloudcue.screens.city_details.model.DetailsState
import repp.max.cloudcue.screens.weather_list.WeatherCardContent
import repp.max.cloudcue.ui.theme.Dimen
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        Modifier.padding(Dimen.base)
    ) {
        LazyRow {
            items(weather.hourly) { hourWeather ->
                TodayForecastItem(hourWeather)
            }
        }
    }
}

@Composable
private fun Forecast(weather: CityWeatherDetails) {
    Timber.d("Forecast: ${weather.daily}")
    Box(
        Modifier.padding(all = Dimen.baseHorizontal)
    ) {
        LazyColumn {
            items(weather.daily) { dayWeather ->
                Card(
                    Modifier
                        .height(54.dp)
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(modifier = Modifier
                            .weight(20f)
                            .padding(start = 12.dp)) {
                            GlideImage(
                                modifier = Modifier.size(30.dp),
                                imageModel = { dayWeather.condition.imageUrl }
                            )
                        }
                        Box(modifier = Modifier.weight(50f)) {
                            Text(
                                fontSize = 15.sp,
                                text = SimpleDateFormat(
                                    "EEEE, d MMMM",
                                    Locale.UK
                                ).format(Date(dayWeather.timestamp))
                            )
                        }
                        Box(modifier = Modifier.weight(15f)) {
                            Text(
                                text = "%dºC".format(dayWeather.temperature.max),

                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        Box(modifier = Modifier.weight(15f)) {
                            Text(
                                text = "%dºC".format(dayWeather.temperature.min),
                                style = TextStyle(fontSize = 13.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TodayForecastItem(thisHour: HourlyForecast) {
    Card(
        Modifier
            .height(88.dp)
            .width(68.dp)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "2pm")
            GlideImage(
                imageModel = { thisHour.condition.imageUrl },
                modifier = Modifier
                    .size(30.dp)
            )
            Text(text = "%dºC".format(thisHour.temperature.current))
        }
    }
}
