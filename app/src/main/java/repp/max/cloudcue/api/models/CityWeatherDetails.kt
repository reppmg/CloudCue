package repp.max.cloudcue.api.models

import com.google.gson.annotations.SerializedName

data class CityWeatherDetailsDto (
    @SerializedName("cnt")
    val count: Long,
    val list: List<HourlyForecastDto>,
)

data class HourlyForecastDto (
    @SerializedName("dt")
    val timestamp: Long,
    val main: MainDto,
    val weather: List<ConditionDto>,
    val clouds: CloudsDto? = null,
    val wind: WindDto? = null,
    val visibility: Long? = null,
    @SerializedName("pop")
    val probabilityOfPrecipitation: Double? = null,
    val rain: RainDto? = null,

    @SerializedName("dt_txt")
    val dtTxt: String? = null
)
