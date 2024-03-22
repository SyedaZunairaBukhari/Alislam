package se.ahemddiya.alislam

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.ahemddiya.alislam.models.AdhanTimingResponse
import se.ahemddiya.alislam.repo.AdhanRepo
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val adhanRepo: AdhanRepo,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val _adhanTimings =
        MutableStateFlow<UiResponse<AdhanTimingResponse>>(UiResponse.Loading)
    val adhanTimings: StateFlow<UiResponse<AdhanTimingResponse>> = _adhanTimings.asStateFlow()

    init {
        fetchAdhanTiming()
    }

    @SuppressLint("MissingPermission")
    fun getLocationBaseAdhanTimings() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            fetchAdhanTiming(it)
        }
    }


    private fun fetchAdhanTiming(location: Location? = null) {
        _adhanTimings.value = UiResponse.Loading
        viewModelScope.launch {
            adhanRepo.getAdhanTiming(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                .onSuccess {
                    _adhanTimings.value = UiResponse.Success(it)
                }.onFailure {
                _adhanTimings.value = UiResponse.Error(it)
            }
        }
    }
}

sealed class UiResponse<out T> {
    data class Success<out T>(val data: T) : UiResponse<T>()
    data class Error(val exception: Throwable) : UiResponse<Nothing>()
    data object Loading : UiResponse<Nothing>()
}