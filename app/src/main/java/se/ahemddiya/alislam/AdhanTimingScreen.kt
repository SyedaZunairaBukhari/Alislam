package se.ahemddiya.alislam

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import se.ahemddiya.alislam.models.AdhanTimingResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AdhanTimingScreen(viewModel: MainActivityViewModel) {


    val uiState by viewModel.adhanTimings.collectAsStateWithLifecycle()

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) {
        when {
            permissionState.allPermissionsGranted -> {
                viewModel.getLocationBaseAdhanTimings()
            }

            permissionState.shouldShowRationale || (!permissionState.allPermissionsGranted && !permissionState.shouldShowRationale) -> {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            is UiResponse.Error -> ShowError((uiState as UiResponse.Error).exception)
            UiResponse.Loading -> ShowLoading()
            is UiResponse.Success -> AdhanTimingSuccess((uiState as UiResponse.Success<AdhanTimingResponse>).data)
        }
    }
}

@Composable
fun AdhanTimingSuccess(data: AdhanTimingResponse) {
    val listState = rememberLazyListState()

    Column {
        Text(text = "Time Zone :: ${data.locationInfo.timezone}")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
        ) {
            items(data.multiDayTimings.first().prayers) { prayer ->
                Text(text = "${prayer.name} :: ${prayer.time.format()}")
                Divider(thickness = 1.dp)
            }
        }
    }

}

private fun Long.format(): String {
    return SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH).format(Date(this))
}

@Composable
fun ShowLoading() {
    CircularProgressIndicator()
}

@Composable
fun ShowError(exception: Throwable) {
    Text(text = exception.localizedMessage ?: "Somthing wrong happened")
}
