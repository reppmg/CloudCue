package repp.max.cloudcue.api.models

import com.google.gson.annotations.SerializedName

data class CityWeatherDetailsDto (
    @SerializedName("cnt")
    val count: Long,
    val list: List<HourlyForecast>,
)

data class HourlyForecast (
    @SerializedName("dt")
    val timestamp: Long,
    val main: Main,
    val weather: List<Condition>,
    val clouds: Clouds? = null,
    val wind: Wind? = null,
    val visibility: Long? = null,
    @SerializedName("pop")
    val probabilityOfPrecipitation: Double? = null,
    val rain: Rain? = null,
    val sys: Sys? = null,

    @SerializedName("dt_txt")
    val dtTxt: String? = null
)
