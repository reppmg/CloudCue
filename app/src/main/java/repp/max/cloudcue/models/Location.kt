package repp.max.cloudcue.models

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    fun isSameCity(other: Location) : Boolean {
        val distanceBetween =
            distanceBetween(this, other)
        return distanceBetween < sameCityThresholdMeters
    }

    private fun convertToRadian(x: Double) = x * Math.PI / 180;

    private fun distanceBetween(loc1: Location, loc2: Location): Double {
        val earthRadius = 6371e3 // in meters

        val lat1Rad = Math.toRadians(loc1.latitude)
        val lat2Rad = Math.toRadians(loc2.latitude)

        val deltaLat = Math.toRadians(loc2.latitude - loc1.latitude)
        val deltaLon = Math.toRadians(loc2.longitude - loc1.longitude)

        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

    companion object {
        private const val sameCityThresholdMeters = 100_000
        fun android.location.Location.toLocation(): Location {
            return Location(latitude, longitude)
        }
    }
}