package repp.max.cloudcue.api

import repp.max.cloudcue.api.models.CityTimeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CityTimeApi {


    @GET("get-time-zone?format=json&by=position")
    suspend fun fetchGmt(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): CityTimeDto
}