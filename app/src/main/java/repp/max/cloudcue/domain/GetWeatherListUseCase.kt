package repp.max.cloudcue.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import repp.max.cloudcue.Config
import repp.max.cloudcue.models.CityWeather
import repp.max.cloudcue.repository.CityWeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val repository: CityWeatherRepository
) {
    suspend operator fun invoke(): Flow<List<CityWeather?>> = flow {
        val value = Config.citiesList.map {
            try {
                repository.loadWeather(it)
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        }
        emit(value)
    }
}