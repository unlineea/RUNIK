package com.example.run.presentation.active_run.maps

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.core.domain.location.Location
import com.example.core.domain.location.LocationTimestamp
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberOverlayManagerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TrackerMap(
    modifier: Modifier = Modifier,
    isRunFinished: Boolean,
    currentLocation: Location?,
    locations: List<List<LocationTimestamp>>,
    onSnapshot: (Bitmap) -> Unit
) {
    var createSnapshotJob: Job? = remember {
        null
    }

    var triggerCapture by remember {
        mutableStateOf(true)
    }

    var mapInstance: MapView? by remember { mutableStateOf(null) }

    val context = LocalContext.current

    val cameraState = rememberCameraState {
        zoom = 17.0
    }

    LaunchedEffect(currentLocation, isRunFinished) {
        if (currentLocation != null && !isRunFinished) {
            cameraState.geoPoint = GeoPoint(currentLocation.lat, currentLocation.long)
        }
    }

    val overlayManagerState = rememberOverlayManagerState()

    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }

    SideEffect {
        mapProperties = mapProperties
            .copy(isTilesScaledToDpi = true)
            .copy(tileSources = TileSourceFactory.MAPNIK)
            .copy(isEnableRotationGesture = false)
            .copy(isFlingEnable = false)
            .copy(zoomButtonVisibility = ZoomButtonVisibility.NEVER)
    }

    OpenStreetMap(
        modifier = modifier.fillMaxSize(),
        cameraState = cameraState,
        properties = mapProperties,
        overlayManagerState = overlayManagerState,
        onFirstLoadListener = {
            mapInstance = overlayManagerState.getMap()
            val copyright = CopyrightOverlay(context)
            overlayManagerState.overlayManager.add(copyright)
        }
    ) {
        RunikPolylines(locations = locations)
    }

    LaunchedEffect(isRunFinished, createSnapshotJob, locations, triggerCapture) {
        if (isRunFinished && triggerCapture && createSnapshotJob == null) {
            triggerCapture = false
            mapInstance?.let { map ->
                try {
                    createSnapshotJob?.cancel()
                    createSnapshotJob = GlobalScope.launch {
                        val bitmap = captureMapScreenshot(map)
                        bitmap.let(onSnapshot)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}


/* Captures the MapView as a Bitmap */
private fun captureMapScreenshot(mapView: MapView): Bitmap {
    val bitmap = createBitmap(mapView.width, mapView.height)
    val canvas = Canvas(bitmap)
    mapView.draw(canvas)

    return bitmap
}