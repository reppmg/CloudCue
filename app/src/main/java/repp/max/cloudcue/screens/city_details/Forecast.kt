package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.ui.theme.Dimen
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun Forecast(weather: CityWeatherDetails) {
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
                        Box(
                            modifier = Modifier
                                .weight(18f)
                                .padding(start = 12.dp)
                        ) {
                            GlideImage(
                                modifier = Modifier.size(30.dp),
                                imageModel = { dayWeather.condition.imageUrl }
                            )
                        }
                        Box(modifier = Modifier.weight(54f)) {
                            Text(
                                fontSize = 14.sp,
                                text = SimpleDateFormat(
                                    "EEEE, d MMMM",
                                    Locale.UK
                                ).format(Date(dayWeather.timestamp * 1000))
                            )
                        }
                        Box(modifier = Modifier.weight(14f)) {
                            Text(
                                text = "%dºC".format(dayWeather.temperature.max),

                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        Box(modifier = Modifier.weight(14f)) {
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