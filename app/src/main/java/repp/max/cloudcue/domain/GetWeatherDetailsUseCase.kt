package repp.max.cloudcue.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import repp.max.cloudcue.models.CityWeatherDetails
import repp.max.cloudcue.repository.CityWeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetWeatherDetailsUseCase @Inject constructor(
    private val repository: CityWeatherRepository
) {
    operator fun invoke(cityName: String) : Flow<CityWeatherDetails> = flow {
        try {
            emit(repository.loadDetails(cityName))
        } catch (e: Exception) {
            Timber.e(e)
            error(e)
        }
    }.flowOn(Dispatchers.IO)
}