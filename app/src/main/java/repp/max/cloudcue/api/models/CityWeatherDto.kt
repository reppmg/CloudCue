package repp.max.cloudcue.api.models

import com.google.gson.annotations.SerializedName


data class CityWeatherDto (
    val coord: Coord? = null,
    @SerializedName("weather")
    val condition: List<Condition>? = null,
    val base: String? = null,
    val main: Main? = null,
    val visibility: Long? = null,
    val wind: Wind? = null,
    val rain: Rain? = null,
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val sys: Sys? = null,
    val timezone: Long? = null,
    val id: Long? = null,
    val name: String? = null,
    val cod: Long? = null
)

data class Clouds (
    val all: Long? = null
)

data class Coord (
    val lon: Double? = null,
    val lat: Double? = null
)

data class Main (
    val temp: Double? = null,

    @SerializedName("feels_like")
    val feelsLike: Double? = null,

    @SerializedName("temp_min")
    val tempMin: Double? = null,

    @SerializedName("temp_max")
    val tempMax: Double? = null,

    val pressure: Long? = null,
    val humidity: Long? = null,

    @SerializedName("sea_level")
    val seaLevel: Long? = null,

    @SerializedName("grnd_level")
    val grndLevel: Long? = null
)

data class Rain (
    @SerializedName("1h")
    val the1H: Double? = null
)

data class Sys (
    val type: Long? = null,
    val id: Long? = null,
    val country: String? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null
)

data class Condition (
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)

data class Wind (
    val speed: Double? = null,
    val deg: Long? = null,
    val gust: Double? = null
)

