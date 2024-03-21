package se.ahemddiya.alislam.repo

import se.ahemddiya.alislam.core.network.NetworkDataSource
import se.ahemddiya.alislam.models.AdhanTimingResponse
import javax.inject.Inject

class AdhanRepo @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {

    suspend fun getAdhanTiming(lat: Double, lng: Double): Result<AdhanTimingResponse> {
        return try {
            val response = networkDataSource.getAdhanTiming(lat, lng)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(RuntimeException("Woops Some error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}