package repp.max.cloudcue.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import repp.max.cloudcue.LocalCityWeatherViewModelFactory
import repp.max.cloudcue.screens.city_details.CityDetailsScreen
import repp.max.cloudcue.screens.city_details.CityDetailsViewModel
import repp.max.cloudcue.screens.weather_list.CityWeatherListScreen
import timber.log.Timber

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.CityWeatherList.route) {
        composable(Screens.CityWeatherList.route) {
            CityWeatherListScreen(navController, hiltViewModel())
        }
        composable(
            Screens.CityWeatherDetails.route,
            arguments = listOf(navArgument("cityId") { type = NavType.StringType })
        ) {
            val viewModelAssistedFactory =
                checkNotNull(LocalCityWeatherViewModelFactory.current)
            val city = requireNotNull(it.arguments?.getString("cityId"))
            CityDetailsScreen(
                viewModel = viewModel(
                    factory = CityDetailsViewModel.factory(viewModelAssistedFactory, city)
                )
            )
        }
    }
}