package repp.max.cloudcue.repository

import okio.IOException
import repp.max.cloudcue.Constants
import repp.max.cloudcue.api.CityTimeApi
import repp.max.cloudcue.api.WeatherApi
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import timber.log.Timber
import javax.inject.Inject

class CityWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityTimeApi: CityTimeApi
) {

    suspend fun loadWeather(cityName: String): CityWeather {
        val city = fetchCity(cityName)

        val weatherDto = weatherApi.getWeatherForPosition(city.latitude, city.longitude)

        val condition = weatherDto.condition?.firstOrNull()
        val conditionName = condition?.description
        return CityWeather(
            city,
            requireNotNull(weatherDto.main?.temp) + Constants.kelvinZero,
            conditionName,
            fetchIconUrlForCondition(condition?.id)
        )
    }

    private suspend fun fetchCity(cityName: String): City {
        val cityLocation = weatherApi.encodeCity(cityName).first()
        Timber.d("loadWeather: $cityLocation")
        requireNotNull(cityLocation.lat)
        requireNotNull(cityLocation.lon)
        val cityGmt = try {
            cityTimeApi.fetchGmt(cityLocation.lat, cityLocation.lon)
        } catch (e: IOException) {
            Timber.w(e, "Error loading city time")
            null
        }
        return City(
            cityName,
            cityLocation.lat,
            cityLocation.lon,
            cityGmt?.gmtOffsetHours
        )
    }

    private suspend fun fetchIconUrlForCondition(conditionId: Int?): String? {
        val id = ConditionIconMapping[conditionId] ?: return null
        return "https://openweathermap.org/img/wn/${id}@2x.png"
    }
}