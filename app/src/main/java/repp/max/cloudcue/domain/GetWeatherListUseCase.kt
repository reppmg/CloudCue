package repp.max.cloudcue.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repp.max.cloudcue.Config
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.models.Location
import repp.max.cloudcue.repository.CityWeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val repository: CityWeatherRepository,
    private val locationProvider: LocationProvider,
    private val singletoneScope: CoroutineScope
) {
    init {
        singletoneScope.launch {
            locationProvider.locationFlow.collect {
                Timber.d("onNewLocation: [${it?.latitude}:${it?.longitude}]")
                if (it != null) {
                    launch(Dispatchers.IO) {
                        repository.loadWeather(
                            Location(
                                it.latitude,
                                it.longitude
                            )
                        )
                    }
                }
            }
        }
    }

    suspend operator fun invoke(): Flow<List<CityWeather>> {
        coroutineScope {
            Timber.d("invoke: subscribe for ${locationProvider.locationFlow} ")
            Timber.d("invoke: AAAAAAA ")
            Config.citiesList.forEach {
                launch(Dispatchers.IO) { repository.loadWeather(it) }
            }
        }
        return repository.weathersFlow
    }
}