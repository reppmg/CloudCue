package repp.max.cloudcue.navigation


sealed class Screens(val route: String) {
    object CityWeatherList : Screens("city_weather_list")
    data class CityWeatherDetails(val city: String) : Screens("city_weather_details/${city}") {
        companion object {
            const val route = "city_weather_details/{cityId}"
        }
    }
}
