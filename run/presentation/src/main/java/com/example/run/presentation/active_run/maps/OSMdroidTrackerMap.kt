package com.example.run.presentation.active_run.maps

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.preference.PreferenceManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.core.domain.location.Location
import com.example.core.domain.location.LocationTimestamp
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.drawing.MapSnapshot
import org.osmdroid.views.overlay.Marker
import timber.log.Timber


@Composable
fun MapView(
    modifier: Modifier = Modifier,
    userLocation: Location?,
    locations: List<List<LocationTimestamp>>,
    isRunFinished: Boolean, // Can be used for UI changes if needed
    onSnapshot: (Bitmap) -> Unit // Callback for taking a screenshot
) {
    var triggerCapture by remember { mutableStateOf(true) }

    Timber.d("recomposed userLocation: ${userLocation.toString()}")

    val mapView = rememberMapViewWithLifecycle()

//    val currentLocation by remember {
//        mutableStateOf()
//    }

//    Timber.d("currentLocation new value: ${currentLocation.toString()}")

    // Remember the marker
    val marker = remember { Marker(mapView) }

    val markerPositionLat by animateFloatAsState(
        targetValue = userLocation?.lat?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )
    val markerPositionLong by animateFloatAsState(
        targetValue = userLocation?.long?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    // Update marker when userLocation changes
    LaunchedEffect(markerPositionLat, markerPositionLong) {
        marker.position = GeoPoint(markerPositionLat.toDouble(), markerPositionLong.toDouble())
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        if (!mapView.overlays.contains(marker)) {
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refresh the map
    }

    LaunchedEffect(isRunFinished) {
        if (isRunFinished) {
            if (triggerCapture) {
                triggerCapture = false
                captureMapSnapshot(
                    mapView,
                    onSnapshot
                )
            }
        }
    }

    // Add polylines when locations list updates
//    LaunchedEffect(locations) {
    OSMRunikPolylines(locations, mapView)

//        if (locations.first().isNotEmpty()) {
//            locations.forEach { rawLines ->
//                val line = mapLocationTimestampsToGeoPoints(rawLines)
//                val polyline = Polyline().apply {
//                    setPoints(line)
//                    color = Color.RED
//                    width = 5f
//                }
//                mapView.overlayManager.add(polyline)
//                mapView.invalidate()
//            }
//        }
//    }


    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = {
            it.controller.setZoom(18.0) // Set zoom level
            it.controller.animateTo(
                GeoPoint(userLocation?.lat?:0.0, userLocation?.long?:0.0)
            ) // Move map smoothly to user location
        }
    )
}


@SuppressLint("RememberReturnType")
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current

    // Configure osmdroid
    remember {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = "MapApp"
    }

    // Create and remember MapView
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    // Handle lifecycle events
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}



@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
    }


fun mapLocationTimestampsToGeoPoints(locationTimestamps: List<LocationTimestamp>): List<GeoPoint> {
    return locationTimestamps.map { timestamp ->
        GeoPoint(timestamp.location.location.lat, timestamp.location.location.long)
    }
}

fun captureMapSnapshot(mapView: MapView, onSnapshot: (Bitmap) -> Unit) {
    // Define the snapshot callback
    val callback = MapSnapshot.MapSnapshotable {
        if (it != null) {
            onSnapshot(it.bitmap)
        } else {
            Timber.d("Failed to capture map snapshot")
        }
    }

    // Create MapSnapshot instance
    MapSnapshot(
        callback,
        MapSnapshot.INCLUDE_FLAGS_ALL,
        mapView
    )
}