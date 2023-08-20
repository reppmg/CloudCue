package repp.max.cloudcue.api.models

import com.google.gson.annotations.SerializedName

data class CityTimeDto(
    val datetime: String,

    @SerializedName("timezone_name")
    val timezoneName: String,

    @SerializedName("timezone_location")
    val timezoneLocation: String,

    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,

    @SerializedName("gmt_offset")
    val gmtOffset: Long,

    @SerializedName("is_dst")
    val isDst: Boolean,

    @SerializedName("requested_location")
    val requestedLocation: String,

    val latitude: Double,
    val longitude: Double
)
