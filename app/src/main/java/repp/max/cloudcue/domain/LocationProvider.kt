package repp.max.cloudcue.domain

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationProvider @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
){
    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow : Flow<Location?> = _locationFlow

    @SuppressLint("MissingPermission")
    suspend fun requestLocation() {
        withContext(Dispatchers.IO) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    Timber.d("requestLocation: new location ${location}")
                    val emit = _locationFlow.tryEmit(location)
                    Timber.d("requestLocation: emitted: $emit to $_locationFlow")
                }
        }
    }
}
