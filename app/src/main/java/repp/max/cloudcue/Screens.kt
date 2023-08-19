package repp.max.cloudcue

sealed class Screens(val route: String) {
    object CityWeatherList: Screens("city_weather_list")
    object CityWeatherDetails:  Screens("city_weather_details")
}
