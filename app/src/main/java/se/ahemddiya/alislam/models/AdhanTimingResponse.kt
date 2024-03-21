package se.ahemddiya.alislam.models

import kotlinx.serialization.Serializable


@Serializable
data class Coordinates(
    var latitude: Double,
    var longitude: Double
)

@Serializable
data class LocationInfo(
    var lat: String,
    var lng: String,
    var timezone: String,
)

@Serializable
data class MultiDayTiming(
    var prayers: ArrayList<Prayer>,
    var coordinates: Coordinates,
    var date: Long
)

@Serializable
data class Prayer(
    var name: String,
    var time: Long,
    var audio: String? = "",
)

@Serializable
data class AdhanTimingResponse(
    var locationInfo: LocationInfo,
    var multiDayTimings: List<MultiDayTiming>
)