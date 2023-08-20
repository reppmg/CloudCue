package repp.max.cloudcue.repository

import repp.max.cloudcue.Constants
import repp.max.cloudcue.api.models.ConditionDto
import repp.max.cloudcue.api.models.HourlyForecastDto
import repp.max.cloudcue.models.Condition
import repp.max.cloudcue.models.HourlyForecast
import repp.max.cloudcue.models.Temperature

fun ConditionDto.toModel(): Condition {
    return Condition(
        requireNotNull(description),
        requireNotNull(fetchIconUrlForCondition(id))
    )

}

fun HourlyForecastDto.toModel(): HourlyForecast {
    return HourlyForecast(
        this.timestamp,
        Temperature(
            (requireNotNull(main.temp) + Constants.kelvinZero).toInt(),
            (requireNotNull(main.tempMin) + Constants.kelvinZero).toInt(),
            (requireNotNull(main.tempMax) + Constants.kelvinZero).toInt()
        ),
        weather.first().toModel()
    )
}

private fun fetchIconUrlForCondition(conditionId: Int?): String? {
    val id = ConditionIconMapping[conditionId] ?: return null
    return "https://openweathermap.org/img/wn/${id}@2x.png"
}