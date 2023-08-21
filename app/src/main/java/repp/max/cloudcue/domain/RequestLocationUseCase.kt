package repp.max.cloudcue.domain

import javax.inject.Inject

class RequestLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke() {
        locationProvider.requestLocation()
    }
}