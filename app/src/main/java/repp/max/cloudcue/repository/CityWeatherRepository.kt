package repp.max.cloudcue.repository

import repp.max.cloudcue.api.WeatherApi
import repp.max.cloudcue.api.models.CityWeatherDto
import repp.max.cloudcue.models.CityWeather
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
        val weatherDto = api.getWeatherForPosition(cityLocation.lat, cityLocation.lon)
        val temp = weatherDto.main?.temp
        requireNotNull(temp)
        return CityWeather(city, temp)
    }
}