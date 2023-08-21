package repp.max.cloudcue.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import repp.max.cloudcue.Config
import repp.max.cloudcue.models.CityWeather
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
            locationProvider.locationFlow
                .collect {
                    Timber.d("onNewLocation: ")
                    if (it != null) {
                        launch(Dispatchers.IO) {
                            repository.loadWeather(it)
                        }
                    }
                }
        }
    }

    suspend operator fun invoke(): Flow<List<CityWeather>> {
        Timber.d("invoke: ")
        coroutineScope {
            Config.citiesList.forEach {
                launch(Dispatchers.IO) { repository.loadWeather(it) }
            }
        }
        return repository.weathersFlow.map {
            val usersLocation = locationProvider.locationFlow.value ?: return@map it
            it.sortedWith { a, b ->
                when {
                    a.city.location.isSameCity(usersLocation) -> -1
                    b.city.location.isSameCity(usersLocation) -> 1
                    else -> a.city.name.compareTo(b.city.name)
                }
            }
        }
    }
}