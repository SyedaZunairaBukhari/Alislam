package se.ahemddiya.alislam.core.network

import retrofit2.Response
import se.ahemddiya.alislam.models.AdhanTimingResponse


interface NetworkDataSource {
    suspend fun getAdhanTiming(lat: Double, lng: Double): Response<AdhanTimingResponse>
}