package repp.max.cloudcue.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
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
import kotlin.coroutines.coroutineContext

@Singleton
class CityWeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityTimeApi: CityTimeApi
) {
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val flowUpdateScope = newSingleThreadContext("weatherFlow")
    private val _weathersFlow = MutableStateFlow<List<CityWeather>>(listOf())
    val weathersFlow: StateFlow<List<CityWeather>> = _weathersFlow

    private val channel =
        Channel<Location>(Channel.UNLIMITED)

    init {
        CoroutineScope(Dispatchers.Default).launch {
            for (item in channel) {
                actualFetchCityTime(item)
                delay(500)
            }
        }
    }

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
        withContext(flowUpdateScope){
            val currentList = _weathersFlow.value
            val newList = currentList.filter { it.city.name != city.name }.plus(cityWeather)
            _weathersFlow.emit(newList)
        }
        Timber.d("loadWeatherForCity:  emitted a new list in $_weathersFlow")
        return cityWeather
    }

    private suspend fun fetchCity(location: Location): City {
        val cityLocationDto =
            weatherApi.decodeCity(location.latitude, location.longitude).firstOrNull()
        fetchCityTime(location)
        return requireNotNull(cityLocationDto?.toCity(null))
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
        fetchCityTime(location)
        return City(
            cityName,
            location,
            null
        )
    }


    //due to 429, async with delay
    private suspend fun fetchCityTime(location: Location) {
        channel.send(location)
    }
    private suspend fun actualFetchCityTime(location: Location) {
        try {
            Timber.d("fetchCityTime: ")
            val cityGmt = cityTimeApi.fetchGmt(location.latitude, location.longitude)
            withContext(flowUpdateScope){
                _weathersFlow.emit(_weathersFlow.value.map { cityWeather ->
                    if (cityWeather.city.location.isSameCity(location)) {
                        cityWeather.copy(
                            city = cityWeather.city.copy(
                                gmtOffset = cityGmt.gmtOffsetHours
                            )
                        )
                    } else {
                        cityWeather
                    }
                })
            }
        } catch (e: Exception) {
            Timber.w(e, "Error loading city time")
        }
    }
}