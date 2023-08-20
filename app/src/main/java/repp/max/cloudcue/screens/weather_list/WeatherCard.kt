package repp.max.cloudcue.screens.weather_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.ui.theme.CloudCueTheme
import repp.max.cloudcue.ui.theme.TransparentWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherCard(weather: CityWeather) {
    Box(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = TransparentWhite
            )
        ) {
            WeatherCardContent(weather)
        }
    }
}

@Composable
fun WeatherCardContent(weather: CityWeather) {
    Row(
        Modifier
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .height(112.dp)) {
        Column(
            modifier = Modifier
                .weight(35f)
                .fillMaxHeight(),
            content = degreesColumn(weather)
        )
        Box(
            modifier = Modifier
                .width(1.5.dp)
                .align(Alignment.Bottom)
                .fillMaxHeight(0.6f)
                .background(Color(0x80FEFEFE))
        )
        Column(
            modifier = Modifier.weight(65f),
            content = locationColumn(weather)
        )
    }
}

@Composable
private fun degreesColumn(weather: CityWeather): @Composable() (ColumnScope.() -> Unit) =
    {
        Text(
            modifier = Modifier,
            text = SimpleDateFormat("EEE dd, MMM", Locale.UK).format(Date()),
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "${weather.currentTemp.toInt()}ºC",
                modifier = Modifier,

                style = TextStyle(fontSize = 42.sp)
            )
        }
    }

@Composable
private fun locationColumn(weather: CityWeather): @Composable() (ColumnScope.() -> Unit) =
    {
        Box(
            Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = TransparentWhite
                ),
                modifier = Modifier
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Outlined.LocationOn, "location", modifier = Modifier.size(22.dp))
                    Text(
                        text = weather.city.name,
                        modifier = Modifier
                            .padding(end = 8.dp, start = 4.dp),
                        fontSize = 14.sp
                    )

                }
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            weather.conditionName?.let { condition ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ConditionRow(condition, weather.conditionImageUrl)
                }
            }
            weather.city.localTime()?.let { localTime ->
                LocalTimeRow(localTime)
            }
        }
    }

@Composable
private fun ConditionRow(condition: String, url: String?) {
    url?.let { imageUrl ->
        GlideImage(
            imageModel = { imageUrl },
            modifier = Modifier.size(26.dp),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Inside
            )
        )
    } ?: Box(modifier = Modifier.size(24.dp))
    Spacer(modifier = Modifier.width(6.dp))
    Text(text = condition, fontSize = 16.sp)
}

@Composable
private fun LocalTimeRow(localTime: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 2.dp)
    ) {
        Icon(
            Icons.Outlined.AccessTime,
            contentDescription = "accessTime",
            Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = localTime, fontSize = 16.sp, modifier = Modifier.padding(start = 2.dp))
    }
}

@Preview
@Composable
fun ListPreview() {
    CloudCueTheme {
        WeatherList(
            {}, listOf(
                CityWeather(
                    City("London", 0.0, 0.0, 1),
                    23.0,
                    "scattered clouds",
                    "http://openweathermap.org/img/wn/50d@2x.png"
                ),
            )
        )
    }
}