package repp.max.cloudcue.domain

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import repp.max.cloudcue.models.Location
import repp.max.cloudcue.models.Location.Companion.toLocation
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_LATITUDE = "lat"
private const val KEY_LONGITUDE = "lon"

@Singleton
class LocationProvider @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val storage: SharedPreferences
) {
    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow: StateFlow<Location?> = _locationFlow

    init {
        val longStr = storage.getString(KEY_LONGITUDE, null)
        val latStr = storage.getString(KEY_LATITUDE, null)
        if (longStr != null && latStr != null) {
            val savedLocation = Location(
                latStr.toDouble(),
                longStr.toDouble()
            )
            val emit = _locationFlow.tryEmit(savedLocation)
            Timber.d("emitted saved location $savedLocation; $emit: ")
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun requestLocation() {
        callbackFlow {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { androidLocation: android.location.Location? ->
                    storage.edit {
                        putString(KEY_LATITUDE, androidLocation?.latitude?.toString())
                        putString(KEY_LONGITUDE, androidLocation?.longitude?.toString())
                    }
                    trySendBlocking(androidLocation?.toLocation())
                }
            awaitClose { }
        }
            .flowOn(Dispatchers.IO)
            .collect {
                val emit = _locationFlow.emit(it)
            }
    }
}
