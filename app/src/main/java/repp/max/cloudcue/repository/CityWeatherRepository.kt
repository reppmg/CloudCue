package repp.max.cloudcue.repository

import repp.max.cloudcue.api.WeatherApi
import repp.max.cloudcue.api.models.CityWeather
import timber.log.Timber
import javax.inject.Inject

class CityWeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun loadWeather(city: String): CityWeather {
        Timber.d("loadWeather: ")
        val cityLocation = api.encodeCity(city).first()
        Timber.d("loadWeather: $cityLocation")
        requireNotNull(cityLocation.lat)
        requireNotNull(cityLocation.lon)
        return api.getWeatherForPosition(cityLocation.lat, cityLocation.lon)
    }
}