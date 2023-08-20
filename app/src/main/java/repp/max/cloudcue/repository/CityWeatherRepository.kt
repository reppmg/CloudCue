package repp.max.cloudcue.repository

import okio.IOException
import repp.max.cloudcue.Constants
import repp.max.cloudcue.api.CityTimeApi
import repp.max.cloudcue.api.WeatherApi
import repp.max.cloudcue.api.models.CityWeatherDetailsDto
import repp.max.cloudcue.api.models.HourlyForecast
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.repository.util.DateUtils
import repp.max.cloudcue.takeEach
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class CityWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityTimeApi: CityTimeApi
) {
    private val cache = LruCache<String, CityWeather>()

    suspend fun loadDetails(cityName: String): CityWeatherDetails {
        Timber.d("loadDetails: context: $coroutineContext")
        val cityWeather = requireNotNull(cache[cityName] ?: loadWeather(cityName))
        val city = cityWeather.city
        val details = weatherApi.getDetails(city.latitude, city.longitude)
        val hourly = details.list.take(24)
        val daily = filter12pmForecasts(details, city.gmtOffset)
        return CityWeatherDetails(
            cityWeather,
            hourly,
            daily
        )
    }

    private fun filter12pmForecasts(
        details: CityWeatherDetailsDto,
        gmtOffset: Long?
    ): List<HourlyForecast> {
        val tomorrowNoon = DateUtils.nextDay12pmDate(gmtOffset ?: 0).time
        return details.list.dropWhile { it.timestamp < tomorrowNoon }.takeEach(8)
    }


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