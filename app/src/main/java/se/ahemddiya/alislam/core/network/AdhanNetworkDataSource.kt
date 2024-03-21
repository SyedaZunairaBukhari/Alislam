package se.ahemddiya.alislam.core.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import se.ahemddiya.alislam.models.AdhanTimingResponse
import javax.inject.Inject
import javax.inject.Singleton


interface AdhanNetworkApi {

    @GET(value = "api/timings/day")
    suspend fun getAdhanTiming(
        @Query("lat") lat: Double?,
        @Query("lng") lang: Double?,
    ): Response<AdhanTimingResponse>

}


@Singleton
class AdhanNetworkDataSource @Inject constructor(
    private val networkApi: AdhanNetworkApi
) : NetworkDataSource {

    override suspend fun getAdhanTiming(lat: Double, lng: Double) =
        networkApi.getAdhanTiming(lat, lng)
}

