package repp.max.cloudcue.api

import repp.max.cloudcue.api.models.CityLocation
import repp.max.cloudcue.api.models.CityWeather
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeatherForPosition(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): CityWeather

    @GET("geo/1.0/direct?limit=1")
    suspend fun encodeCity(
        @Query("q") cityName: String
    ): List<CityLocation>


    @GET("geo/1.0/reverse?limit=1")
    suspend fun decodeCity(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): List<CityLocation>

}