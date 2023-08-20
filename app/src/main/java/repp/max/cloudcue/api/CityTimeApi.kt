package repp.max.cloudcue.api

import repp.max.cloudcue.api.models.CityTimeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CityTimeApi {


    @GET("https://timezone.abstractapi.com/v1/current_time/")
    suspend fun fetchGmt(
        @Query("location") cityName: String
    ): CityTimeDto
}