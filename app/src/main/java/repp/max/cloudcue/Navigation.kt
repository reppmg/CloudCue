package repp.max.cloudcue

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import repp.max.cloudcue.screens.CityWeatherListScreen
import repp.max.cloudcue.screens.CityWeatherViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.CityWeatherList.route) {
        composable(Screens.CityWeatherList.route) {
            CityWeatherListScreen(navController, hiltViewModel())
        }
        composable(Screens.CityWeatherDetails.route) {

        }
    }
}