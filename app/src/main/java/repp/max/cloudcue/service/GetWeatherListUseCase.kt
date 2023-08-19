package repp.max.cloudcue.service

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor() {

    suspend fun getList(): Flow<List<String>> {
        return callbackFlow {
            trySendBlocking(listOf(
                "Perm: -30",
                "Tbilisi: +60",
                "Milton: -80"
            ))
            awaitClose()
        }
    }
}