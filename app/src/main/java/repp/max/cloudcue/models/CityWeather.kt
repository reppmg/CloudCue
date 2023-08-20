package repp.max.cloudcue.models

data class CityWeather(
    val cityName: String,
    val currentTemp: Double,

) {
    override fun toString(): String {
        return "$cityName: $currentTemp"
    }
}
