package repp.max.cloudcue.screens.city_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import repp.max.cloudcue.models.HourlyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun TodayForecastItem(thisHour: HourlyForecast) {
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
            Text(text = SimpleDateFormat("HH:00", Locale.UK).format(Date(thisHour.timestamp * 1000)))
            GlideImage(
                imageModel = { thisHour.condition.imageUrl },
                modifier = Modifier
                    .size(30.dp)
            )
            Text(text = "%dºC".format(thisHour.temperature.current))
        }
    }
}