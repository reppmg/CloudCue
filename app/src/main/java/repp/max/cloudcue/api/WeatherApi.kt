package repp.max.cloudcue.api

import repp.max.cloudcue.api.models.CityLocationDto
import repp.max.cloudcue.api.models.CityWeatherDetailsDto
import repp.max.cloudcue.api.models.CityWeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeatherForPosition(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): CityWeatherDto

    @GET("geo/1.0/direct?limit=1")
    suspend fun encodeCity(
        @Query("q") cityName: String
    ): List<CityLocationDto>


    @GET("geo/1.0/reverse?limit=1")
    suspend fun decodeCity(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): List<CityLocationDto>

    @GET("data/2.5/forecast")
    suspend fun getDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): CityWeatherDetailsDto

}