package repp.max.cloudcue.models

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    fun isSameCity(other: Location) : Boolean {
        return getDistanceBetween(latitude, longitude, other.latitude, other.longitude) < sameCityThresholdMeters
    }

    fun convertToRadian(x: Double) = x * Math.PI / 180;

    fun getDistanceBetween(lat1: Double, lat2: Double, lng1: Double, lng2: Double): Double {
        val R = 6378137; // Earth’s mean radius in meter
        val dLat = convertToRadian(lat2 - lat1);
        val dLong = convertToRadian(lng2 - lng1);
        val a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(convertToRadian(lat1)) * Math.cos(
                convertToRadian(lat2)
            ) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // returns the distance in meter
    }

    companion object {
        private const val sameCityThresholdMeters = 10_000
    }
}