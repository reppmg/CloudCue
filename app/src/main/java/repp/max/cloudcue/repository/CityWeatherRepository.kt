package repp.max.cloudcue.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.IOException
import repp.max.cloudcue.Constants
import repp.max.cloudcue.api.CityTimeApi
import repp.max.cloudcue.api.WeatherApi
import repp.max.cloudcue.api.models.CityTimeDto
import repp.max.cloudcue.api.models.CityWeatherDetailsDto
import repp.max.cloudcue.api.models.HourlyForecastDto
import repp.max.cloudcue.models.City
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.models.Location
import repp.max.cloudcue.repository.util.DateUtils
import repp.max.cloudcue.takeEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityTimeApi: CityTimeApi
) {
    private val _weathersFlow = MutableStateFlow<List<CityWeather>>(listOf())
    val weathersFlow: StateFlow<List<CityWeather>> = _weathersFlow

    suspend fun loadDetails(cityName: String): CityWeatherDetails {
        val cityWeather = loadWeather(cityName)
        val city = cityWeather.city
        val details = weatherApi.getDetails(city.location.latitude, city.location.longitude)
        val hourly = details.list.take(8).map(HourlyForecastDto::toModel)
        val daily = filterForecastFromTomorrow(details, city.gmtOffset)
            .chunked(8)
            .map { dayChunk ->
                dayChunk[dayChunk.size / 2].run {
                    copy(
                        main = main.copy(
                            tempMin = dayChunk.minBy { requireNotNull(it.main.tempMin) }.main.tempMin,
                            tempMax = dayChunk.maxBy { requireNotNull(it.main.tempMax) }.main.tempMax
                        )
                    )
                }.toModel()
            }
        return CityWeatherDetails(
            cityWeather,
            hourly,
            daily
        )
    }

    private fun filterForecastFromTomorrow(
        details: CityWeatherDetailsDto,
        gmtOffset: Long?
    ): List<HourlyForecastDto> {
        val tomorrowMidnight = DateUtils.nextDayMidnightDate(gmtOffset ?: 0).time / 1000
        return details.list.dropWhile { it.timestamp < tomorrowMidnight }
    }

    suspend fun loadWeather(location: Location): CityWeather {
        val city = weathersFlow.value
            .find { it.city.location.isSameCity(location) }?.city ?: fetchCity(location)

        return loadWeatherForCity(city)
    }

    suspend fun loadWeather(cityName: String): CityWeather {
        Timber.d("loadWeather: ")
        val city = weathersFlow.value
            .find { it.city.name == cityName }?.city ?: fetchCity(cityName)

        val loadWeatherForCity = loadWeatherForCity(city)
        Timber.d("loadWeather: end")
        return loadWeatherForCity
    }

    private suspend fun loadWeatherForCity(
        city: City
    ): CityWeather {
        val weatherDto =
            weatherApi.getWeatherForPosition(city.location.latitude, city.location.longitude)

        val condition = requireNotNull(weatherDto.condition).first()

        val cityWeather = CityWeather(
            city,
            requireNotNull(weatherDto.main?.temp) + Constants.kelvinZero,
            condition.toModel()
        )
        val currentList = _weathersFlow.value
        val newList = currentList.filter { it.city.name != city.name }.plus(cityWeather)
        _weathersFlow.emit(newList)
        Timber.d("loadWeatherForCity:  emitted a new list in $_weathersFlow")
        return cityWeather
    }

    private suspend fun fetchCity(location: Location): City {
        val cityLocationDto =
            weatherApi.decodeCity(location.latitude, location.longitude).firstOrNull()
        val gmtOffset = fetchCityTime(location)?.gmtOffsetHours
        return requireNotNull(cityLocationDto?.toCity(gmtOffset))
    }

    private suspend fun fetchCity(cityName: String): City {
        val cityLocation = weatherApi.encodeCity(cityName).first()
        Timber.d("loadWeather: $cityLocation")
        requireNotNull(cityLocation.lat)
        requireNotNull(cityLocation.lon)
        val location = Location(
            cityLocation.lat,
            cityLocation.lon,
        )
        val cityGmt = fetchCityTime(location)
        return City(
            cityName,
            location,
            cityGmt?.gmtOffsetHours
        )
    }

    private suspend fun fetchCityTime(location: Location): CityTimeDto? {
        val cityGmt = try {
            cityTimeApi.fetchGmt(location.latitude, location.longitude)
        } catch (e: IOException) {
            Timber.w(e, "Error loading city time")
            null
        }
        return cityGmt
    }

}