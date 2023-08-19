package repp.max.cloudcue

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import repp.max.cloudcue.screens.city_details.CityDetailsViewModel


val LocalCityWeatherViewModelFactory: ProvidableCompositionLocal<CityDetailsViewModel.AssistedViewModelFactory?> =
    compositionLocalOf { null }