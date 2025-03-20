package com.example.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import com.example.core.domain.location.LocationTimestamp
import com.utsman.osmandcompose.Polyline
import com.utsman.osmandcompose.PolylineCap
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun RunikPolylines(locations: List<List<LocationTimestamp>>) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timestamp1, timestamp2 ->
                PolylineUi(
                    location1 = timestamp1.location.location,
                    location2 = timestamp2.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        location1 = timestamp1,
                        location2 = timestamp2
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                geoPoints = listOf(
                    GeoPoint(polylineUi.location1.lat, polylineUi.location1.long),
                    GeoPoint(polylineUi.location2.lat, polylineUi.location2.long),
                ),
                color = polylineUi.color,
                cap = PolylineCap.ROUND
            )
        }
    }
}

@Composable
fun OSMRunikPolylines(locations: List<List<LocationTimestamp>>, mapView: MapView) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timestamp1, timestamp2 ->
                PolylineUi(
                    location1 = timestamp1.location.location,
                    location2 = timestamp2.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        location1 = timestamp1,
                        location2 = timestamp2
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            val polyline = org.osmdroid.views.overlay.Polyline().apply {
                setPoints(
                    listOf(
                    GeoPoint(polylineUi.location1.lat, polylineUi.location1.long),
                    GeoPoint(polylineUi.location2.lat, polylineUi.location2.long)
                    )
                )
                color = polylineUi.color.toArgb()
                width = 5f
            }
            mapView.overlayManager.add(polyline)
            mapView.invalidate()
        }
    }
}